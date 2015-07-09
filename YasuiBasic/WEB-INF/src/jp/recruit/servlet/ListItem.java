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

import jp.recruit.logic.ListItemLogic;
import jp.recruit.bean.ItemBean;

public class ListItem extends HttpServlet {
	private static final long serialVersionUID = -5390887797210114074L;

	protected void doGet(HttpServletRequest req , HttpServletResponse res)
			throws ServletException,IOException {
		ServletContext sc=null;
		
		String destination= "/WEB-INF/jsp/purchase/list.jsp";
		//商品一覧のArrayList作成
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//ロジッククラスのインスタンス作成
		ListItemLogic listItemLogic = new ListItemLogic();

		//セッションの取得
		HttpSession session = req.getSession(false);
		//戻るボタンを押された場合のオーダーを含むItem情報Listを取得
		@SuppressWarnings("unchecked")
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
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , e.getMessage());
			return;
		}

		//商品一覧は一時的なものなので、リクエストにセットしておく
		req.setAttribute("items", itemList);
		//エラーをセッションに格納
		session.setAttribute("errormessage",error);

		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(req, res);
	}
	protected void doPost(HttpServletRequest req , HttpServletResponse res)
			throws ServletException,IOException {
		this.doGet(req, res);
	}
}
