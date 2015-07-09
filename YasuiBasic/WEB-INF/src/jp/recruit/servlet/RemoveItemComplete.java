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
import jp.recruit.logic.RemoveItemLogic;

public class RemoveItemComplete extends HttpServlet {
	private static final long serialVersionUID = 1059705617163017431L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		ServletContext sc=null;
		String destination=null;
		destination = "/WEB-INF/jsp/removeItem/RemoveItemComplete.jsp";
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//セッションの取得
		HttpSession session = request.getSession(false);
		//エラーが取得できる場合は初期化
		session.removeAttribute("errormessage");
		//ロジックのインスタンス生成
		RemoveItemLogic logic = new RemoveItemLogic();
		
		ArrayList<ItemBean> targetItems = (ArrayList<ItemBean>)session.getAttribute("targetItems");
		if ( targetItems != null ){
			try{
				logic.removeItem(targetItems);
			}catch(SQLException|NamingException e){
				error.add("(RemoveItemCompleteServlet)削除処理が失敗しました。DAOの呼び出しに失敗している可能性があります。");
			}
		}else{
			error.add("(RemoveItemCompleteServlet)削除対象の商品リストを取得できませんでした。");
			
		}
		session.removeAttribute("targetItems");
		if(!error.isEmpty()){
			destination="/RemoveItem";
			//完成したエラーメッセージ用ArrayListをセッションに格納
			session.setAttribute("errormessage",error);
		}
		
		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}

	
}
