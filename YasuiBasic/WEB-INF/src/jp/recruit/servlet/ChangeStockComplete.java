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
import jp.recruit.logic.ChangeStockLogic;

public class ChangeStockComplete extends HttpServlet {
	private static final long serialVersionUID = 2581432951251555792L;

	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		ServletContext sc=null;
		String destination=null;
		//デフォルトの転送先
		destination = "/WEB-INF/jsp/changeStock/ChangeStockComplete.jsp";
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//セッションの取得（なければ作成）
		HttpSession session = request.getSession(false);
		session.removeAttribute("errormessage");
		ChangeStockLogic logic = new ChangeStockLogic();

		ArrayList<ItemBean> stockitems = new ArrayList<ItemBean>();
		int result=0;
		stockitems = (ArrayList<ItemBean>)session.getAttribute("changeStock");

		try{
			result=logic.updateStock(stockitems);
			if(result < 0){
				error.add("(ChangeStockComplete)在庫が更新できませんでした。updateの処理が正常に終了していません。");
			}
		}catch(SQLException | NamingException e){
			error.add("(ChangeStockComplete)在庫の更新ができませんでした。DAOの呼び出しに失敗している可能性があります。");
		}
		
		session.removeAttribute("changeStock");
		if(!error.isEmpty()){
			destination="/ChangeStock";
			session.setAttribute("update", Boolean.valueOf(false));
		}else{
			session.setAttribute("update", Boolean.valueOf(true));
		}
		session.removeAttribute("changeStock");
		
		//完成したエラーメッセージ用ArrayListをセッションに格納
		session.setAttribute("errormessage",error);
		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}
}
