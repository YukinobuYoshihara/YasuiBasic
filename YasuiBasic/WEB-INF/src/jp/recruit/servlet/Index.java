package jp.recruit.servlet;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Index extends HttpServlet{

	private static final long serialVersionUID = -8744723537486968453L;
	protected void doGet(HttpServletRequest req , HttpServletResponse res)
			throws ServletException,IOException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession(true);
		Object error = session.getAttribute("errormessage");
		session.invalidate();
		//セッション取得（なければ作成）
		session = req.getSession(true);
		if(error!=null)
			session.setAttribute("errormessage", error);

		ServletContext sc=null;
		String destination=null;
		//ログアウト処理が成功に終わった時の転送先
		destination = "/WEB-INF/jsp/index.jsp";
		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(req, res);
	}
}