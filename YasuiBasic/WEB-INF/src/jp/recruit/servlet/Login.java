package jp.recruit.servlet;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import jp.recruit.bean.ContentsBean;
import jp.recruit.bean.UserBean;
import jp.recruit.logic.ListContentsLogic;
import jp.recruit.logic.LoginCheckLogic;
public class Login extends HttpServlet {
	private static final long serialVersionUID = -856700274893456786L;

	protected void doPost(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		//ログインフラグを設定（ログイン成功時のみtrueになる）
		Boolean isLogin = false;

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//エラー用のArrayList
		ArrayList<String> error = new ArrayList<String>();
		ServletContext sc=null;
		String destination=null;
		//セッションの取得
		HttpSession session = request.getSession(true);
		//エラーを引き継ぐ場合にArrayListにマージ処理
		@SuppressWarnings("unchecked")
		ArrayList<String> temp = (ArrayList<String>)session.getAttribute("errormessage");
		if(temp != null){
			for(String message:temp){
				error.add(message);
			}
			session.removeAttribute("errormessage");
		}
		//ログイン処理が成功に終わった時の転送先
		destination = "/ListItem";
		//ユーザーの基本情報
		String username="";
		String password="";
		//使用するオブジェクトの初期化
		UserBean userBean= null;

		HashMap<String,ContentsBean> contentsMap = null;

		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//顧客IDの取得
		username=request.getParameter("username").trim();
		//パスワードの取得
		password=request.getParameter("password").trim();

		//ユーザー名とパスワードが空でなかったらログインチェック
		if((username!=null&&!username.isEmpty())&&(password!=null&&!password.isEmpty())){
			try{
				LoginCheckLogic loginCheck = new LoginCheckLogic();
				//データベースに接続して、ユーザー情報を取得
				userBean = loginCheck.authCheck(username, password);
				//ユーザーが存在した
				if(userBean!=null){
					isLogin=true;
					//セッションを再作成する
					session.invalidate();
					session = request.getSession(true);
					//ログイン属性詰め直し
					session.setAttribute("username", userBean.getName());
					session.setAttribute("descript", userBean.getDescript());
					session.setAttribute("id", userBean.getId());
					session.setAttribute("role", userBean.getRole());
					if(userBean.getRole().equalsIgnoreCase("administrator")){
						destination="/AddItem";
					}else{
						destination="/ListItem";
					}
				}else{
					//ユーザー名かパスワードが間違っている場合の処理
					error.add("ログイン処理に失敗しました。ユーザー名とパスワードが間違っている可能性があります");
				}
			}catch(SQLException|NamingException e){
				error.add("(LoginServlet)ログイン処理に失敗しました。データベースに障害が発生している可能性があります"+e.getMessage());
			}
		}else{//ユーザー名とパスワードが空だった場合の処理
			error.add("(LoginServlet)ログイン処理に失敗しました。ユーザー名とパスワードは省略できません。");
		}
		if(isLogin&&error.isEmpty()){
			
		}else{
			//最終的になんらかの障害が発生している
			error.add("(LoginServlet)ログインに失敗しました。");
			session.setAttribute("errormessage",error);
			destination="/WEB-INF/jsp/common/LoginError.jsp";
		}
		
		try{
			//データベースに接続して、TDK情報を管理する情報を取得
			ListContentsLogic listContents = new ListContentsLogic();
			contentsMap = listContents.getContentsMap();
		}catch(SQLException|NamingException e){
			error.add("(LoginServlet)コンテンツの取得処理に失敗しました"+e.getMessage());
			destination="/index";
		}

		//コンテンツ情報をセッションに設定
		session.setAttribute("contents", contentsMap);

		if(!error.isEmpty()){//異常系
			request.setAttribute("errormessage", error);
			System.out.println("リダイレクト先："+request.getContextPath()+destination);
			//異常系はエラーメッセージを持たせるため、フォワード
			sc.getRequestDispatcher(destination).forward(request, response);
			return;
		}else{//正常系
			System.out.println("リダイレクト先："+request.getContextPath()+destination);
			response.sendRedirect(request.getContextPath()+destination);
			return;			
		}
		

	}
}

