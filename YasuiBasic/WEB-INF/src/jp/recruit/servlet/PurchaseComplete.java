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
import jp.recruit.exception.ProcessOrderException;
import jp.recruit.logic.StockUpdateLogic;

public class PurchaseComplete extends HttpServlet {

	private static final long serialVersionUID = -6036220739670048826L;

	protected void doPost(HttpServletRequest req , HttpServletResponse res)
			throws ServletException,IOException {
		ServletContext sc=null;
		String destination=null;
		destination = "/WEB-INF/jsp/purchase/complete.jsp";
		//セッションの取得
		HttpSession session = req.getSession(false);
		//エラーメッセージ処理List
		ArrayList<String> error = new ArrayList<String>();
		session.removeAttribute("errormessage");

		@SuppressWarnings("unchecked")
		ArrayList<ItemBean> orderitems = (ArrayList<ItemBean>)session.getAttribute("items");
		StockUpdateLogic logic = new StockUpdateLogic();
		//処理の結果を受け取る変数
		int result=0;
		try{
			result = logic.updateStock(orderitems, (String)session.getAttribute("username"));
			System.err.println("(purchaseComplete)戻り値："+result);
		}catch(SQLException|NamingException | ProcessOrderException e){
			error.add("(purchaseComplete)エラー："+e.getMessage()+"エラーコード："+result);
			//ロジックのエラー吸い出し
			error.add("エラーの詳細：");
			error.addAll(logic.getErrors());
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , e.getMessage());
			return;
		}
		session.removeAttribute("orderitems");
		session.removeAttribute("items");

		//エラーをセッションに格納
		session.setAttribute("errormessage",error);
		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理を転送
		rd.forward(req, res);
	}
}
