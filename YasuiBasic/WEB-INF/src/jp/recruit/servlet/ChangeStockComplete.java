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
		//エラーメッセージの一時保存用変数
		String message=null;
		//デフォルトの転送先
		String destination = "/WEB-INF/jsp/changeStock/ChangeStockComplete.jsp";
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//セッションの取得（なければ作成）
		HttpSession session = request.getSession(false);
		//在庫更新のロジック
		ChangeStockLogic logic = new ChangeStockLogic();

		int result=0;
		//セッションから
		ArrayList<ItemBean> updatedItemList = (ArrayList<ItemBean>)session.getAttribute("changeStock");

		try{
			result=logic.updateStock(updatedItemList);
			if(result < 0){
				message="(ChangeStockComplete)在庫が更新できませんでした。updateの処理が正常に終了していません。";
				error.add(message);
			}
		}catch(SQLException | NamingException e){
			message="(ChangeStockComplete)在庫の更新ができませんでした。DAOの呼び出しに失敗している可能性があります。";
			error.add(message);
		}
		
		session.removeAttribute("changeStock");
		if(!error.isEmpty()){
			destination="/ChangeStock";
			session.setAttribute("update", Boolean.valueOf(false));
			//完成したエラーメッセージ用ArrayListをrequestに格納
			request.setAttribute("errormessage",error);
		}
		session.removeAttribute("changeStock");
		

		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}
}
