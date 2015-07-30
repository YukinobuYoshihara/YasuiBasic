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

	protected void doGet(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		ServletContext sc=null;
		//デフォルトの遷移先
		String destination= "/WEB-INF/jsp/purchase/list.jsp";
		//商品一覧のArrayList作成
		ArrayList<ItemBean> itemList = new ArrayList<ItemBean>();
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//ロジッククラスのインスタンス作成
		ListItemLogic listItemLogic = new ListItemLogic();

		//セッションの取得
		HttpSession session = request.getSession(false);
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
			//ユーザー系画面では安易に例外をスローせず、エラーメッセージの処理を行う
			e.printStackTrace();
			error.add("初期の商品情報一覧取得に失敗しました");
			destination="/index";
		}

		//商品一覧は一時的なものなので、リクエストにセットしておく
		request.setAttribute("items", itemList);
		//エラーが空じゃなかったら
		if(!error.isEmpty()){
			//エラーをセッションに格納
			request.setAttribute("errormessage",error);
		}
		
		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}
	protected void doPost(HttpServletRequest req , HttpServletResponse res)
			throws ServletException,IOException {
		this.doGet(req, res);
	}
}
