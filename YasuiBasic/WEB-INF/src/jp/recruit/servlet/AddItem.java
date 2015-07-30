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

import jp.recruit.dao.ItemDao;

public class AddItem extends HttpServlet {
  private static final long serialVersionUID = -4971372719367601902L;
  String nextId=null;

  protected void doGet(HttpServletRequest request , HttpServletResponse response)
      throws ServletException,IOException {
    ServletContext sc=null;
    String destination=null;
    //デフォルトの転送先
    destination = "/WEB-INF/jsp/addItem/AddItem.jsp";
    //エラーメッセージ処理クラスのインスタンス化
    ArrayList<String> error = new ArrayList<String>();

    //追加するIDを準備する
    try{
      ItemDao dao = new ItemDao();
      dao.getConnection();
      nextId = dao.getNextItemIdBySequence();
      dao.closeConnection();
    } catch (NamingException|SQLException e) {
    	String messageString = "(AddItem)"+e.getMessage()+":新規商品IDの取得で不具合が発生しています";
		error.add(messageString);
		e.printStackTrace();
		throw new ServletException(messageString, e);
	}
    //商品IDをRequestに格納
    request.setAttribute("nextId", nextId);
    //完成したエラーメッセージ用ArrayListをrequestに格納
    request.setAttribute("errormessage",error);
    //ServletContextオブジェクトを取得
    sc = this.getServletContext();
    //RequestDispatcherオブジェクトを取得
    RequestDispatcher rd = sc.getRequestDispatcher(destination);
    //forwardメソッドで、処理をreceive.jspに転送
    rd.forward(request, response);
  }

}
