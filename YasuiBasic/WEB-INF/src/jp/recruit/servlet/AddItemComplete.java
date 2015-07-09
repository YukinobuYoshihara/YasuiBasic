package jp.recruit.servlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import jp.recruit.bean.ItemBean;
import jp.recruit.exception.ConsistencyErrorException;
import jp.recruit.exception.ItemNotUniqueException;
import jp.recruit.exception.ValidationErrorException;
import jp.recruit.logic.AddItemLogic;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddItemComplete extends HttpServlet {
	private static final long serialVersionUID = 2199827702924298591L;

	protected void doPost(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		ServletContext sc=null;
		String destination=null;
		//デフォルトの転送先
		destination = "/WEB-INF/jsp/addItem/AddItemComplete.jsp";

		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//セッションの取得
		HttpSession session = request.getSession(false);
		session.removeAttribute("errormessage");

		//作業用Beanの生成
		ItemBean newItem  = new ItemBean();
		//ロジッククラスのインスタンス生成
		AddItemLogic logic = new AddItemLogic();
		//結果保持用変数
		int result=0;

		//セッションから新規商品のBeanを取得
		newItem = (ItemBean)session.getAttribute("newItem");
		
		if(newItem==null){
			error.add("セッションから新規商品が取得できませんでした。");
		}else{
			try{
				result = logic.insertItem(newItem);//ロジック呼び出し
				//結果が0以下ならエラーメッセージを取得
				if(result<=0){
					error.addAll(logic.getErrors());
				}
			}catch(SQLException|NamingException | ItemNotUniqueException | ValidationErrorException | ConsistencyErrorException e){
				error.add("(AddItemComplete)商品の追加に失敗しました。(根本原因）："+e.getMessage());
			}
			session.removeAttribute("newItem");
		}
		if(!error.isEmpty()){
			destination="/AddItem";
			//完成したエラーメッセージ用ArrayListをセッションに格納
			session.setAttribute("errormessage",error);
		}
		//不要になったitemsを削除
		session.removeAttribute("items");

		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}

}
