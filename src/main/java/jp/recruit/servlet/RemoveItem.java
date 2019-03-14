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

public class RemoveItem extends HttpServlet {
	private static final long serialVersionUID = -4751992084637285363L;

	protected void doGet(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		ServletContext sc=null;
		String destination="/WEB-INF/jsp/removeItem/RemoveItem.jsp";
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//セッションの取得
		HttpSession session = request.getSession(false);
		session.removeAttribute("canRemove");
		session.removeAttribute("items");
		
		//商品一覧のArrayList作成
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		//ロジッククラスのインスタンス作成
		ListItemLogic listItemLogic = new ListItemLogic();
		//商品情報一覧の取得
		try{
			itemList=listItemLogic.getItemList();
		}catch(SQLException|NamingException|IOException e){
			e.printStackTrace();
			error.add("削除対象の商品情報一覧を取得できませんでした"+e.getMessage());
		}
		if(!error.isEmpty()){
			//完成したエラーメッセージ用ArrayListをRequestに格納
			request.setAttribute("errormessage",error);
			//トップ画面に転送
			destination="/index";
		}
		
		//変更対象の商品のArrayListをrequestに格納
		request.setAttribute("items", itemList);
		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}
	protected void doPost(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		this.doGet(request, response);
	}
}
