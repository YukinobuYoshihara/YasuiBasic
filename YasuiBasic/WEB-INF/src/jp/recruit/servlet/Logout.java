package jp.recruit.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Logout extends YasuiServlet {

  private static final long serialVersionUID = -2936421374161417176L;

  protected void doGet(HttpServletRequest req , HttpServletResponse res)
      throws ServletException,IOException {
    req.setCharacterEncoding("UTF-8");
    res.setCharacterEncoding("UTF-8");
    ServletContext sc=null;
    String destination=null;
    //ログアウト処理が成功に終わった時の転送先
    destination = "/WEB-INF/jsp/logout/Logout.jsp";
    HttpSession session = req.getSession(false);
    if(session!=null){
      session.invalidate();
    }
    //ServletContextオブジェクトを取得
    sc = this.getServletContext();
    //RequestDispatcherオブジェクトを取得
    RequestDispatcher rd = sc.getRequestDispatcher(destination);
    //forwardメソッドで、処理をreceive.jspに転送
    rd.forward(req, res);
  }
}
