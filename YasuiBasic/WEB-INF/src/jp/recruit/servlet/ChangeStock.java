package jp.recruit.servlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.recruit.bean.ItemBean;
import jp.recruit.logic.ListItemLogic;

public class ChangeStock extends HttpServlet {
  private static final long serialVersionUID = -7228890911479890546L;
	@SuppressWarnings("unchecked")
  protected void doGet(HttpServletRequest request , HttpServletResponse response)
      throws ServletException,IOException {
    ServletContext sc=null;
    String destination= "/WEB-INF/jsp/changeStock/ChangeStock.jsp";
    //エラーメッセージ処理クラスのインスタンス化
    ArrayList<String> error = new ArrayList<String>();
    //セッションの取得（なければ作成）
    HttpSession session = request.getSession(false);
    session.removeAttribute("items");
    session.removeAttribute("changeStock");

	//商品一覧のArrayList作成
	ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
	//ロジッククラスのインスタンス作成
	ListItemLogic listItemLogic = new ListItemLogic();

	//戻るボタンを押された場合のオーダーを含むItem情報Listを取得
	ArrayList<ItemBean> backwardList = (ArrayList<ItemBean>)session.getAttribute("items");
	session.removeAttribute("items");
	
	//セッションからitemsを取得できた場合は戻るボタンを押されている
	try{
		if(backwardList!=null){
			itemList=listItemLogic.getItemList(backwardList);
		}else{
			itemList=listItemLogic.getItemList();
		}
	}catch(SQLException|NamingException|IOException e){
		e.printStackTrace();
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , e.getMessage());
		return;
	}

    //完成したエラーメッセージ用ArrayListをセッションに格納
    request.setAttribute("errormessage",error);
    //変更対象の商品のArrayListをセッションに格納
    session.setAttribute("changeStock", itemList);
    //ServletContextオブジェクトを取得
    sc = this.getServletContext();
    //RequestDispatcherオブジェクトを取得
    RequestDispatcher rd = sc.getRequestDispatcher(destination);
    //forwardメソッドで、処理をreceive.jspに転送
    rd.forward(request, response);
  }

}
