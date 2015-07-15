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
		String destination=null;
		destination = "/WEB-INF/jsp/removeItem/RemoveItem.jsp";
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

		//セッションからitemsを取得できた場合は戻るボタンを押されている
		try{
			itemList=listItemLogic.getItemList();
		}catch(SQLException|NamingException|IOException e){
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , e.getMessage());
			return;
		}
		//完成したエラーメッセージ用ArrayListをRequestに格納
		request.setAttribute("errormessage",error);
		//変更対象の商品のArrayListをsessionに格納
		session.setAttribute("items", itemList);
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
