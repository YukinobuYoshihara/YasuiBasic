package jp.recruit.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.recruit.bean.ContentsBean;
/**
 * ログイン時のみに適用するサーブレットの前処理をまとめたフィルター
 */
public class AuthFilter implements Filter {

  @Override
  public void destroy() {

  }

  @SuppressWarnings("unchecked")
  @Override
  public void doFilter(ServletRequest req, ServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    //doFilterの引数がServletRequestなのでHttpServletRequestに変換
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    //エンコーディングを指定
    req.setCharacterEncoding("UTF-8");
    res.setCharacterEncoding("UTF-8");
    res.setContentType("text/html;charset=UTF-8");

    //セッションから情報取得
    HttpSession session = request.getSession();
    String user = (String)session.getAttribute("username");
    String userRole = (String)session.getAttribute("role");
    HashMap<String,ContentsBean> contents =  (HashMap<String,ContentsBean>)session.getAttribute("contents");

    //サーブレットのパス＝コンテンツのキーを取得
    String key = request.getServletPath();
    key = key.substring(1);
    System.out.println("(AuthFilter)サーブレットのパス："+key);

    String title = null;
    String keywd = null;
    String description = null;
    String pageRole = null;
    ContentsBean content = null;
    if(contents!=null){
    	content = contents.get(key);
    }
    if(content != null){
      title = content.getTitle();
      keywd = content.getKeywd();
      description = content.getDescript();
      pageRole = content.getRole();
      session.setAttribute("title", title);
      session.setAttribute("keywd", keywd);
      session.setAttribute("description", description);
    }
    System.out.println("フィルターで取得したタイトル"+title);

    ArrayList<String> error = (ArrayList<String>)session.getAttribute("errormessage");
    if(error==null){
      error=new ArrayList<String>();
    }
    if (user == null || contents == null) {
      //未ログインの場合
      System.out.println("Login check NG");
      error.add("本サービスの利用にはログインが必要です。ログインしてください。");
      session.setAttribute("errormessage", error);
      response.sendRedirect("/YasuiBasic/index");
      return;
    } else {
      //ログイン済みの場合
      System.out.println("Login check OK");
      System.out.println("ユーザーのROLE："+userRole);
      System.out.println("ページのROLE："+pageRole);
      if(pageRole.equals("administrator")&&userRole.equals("user")){
          System.out.println("Role check NG");
          error.add("管理画面には管理者以外アクセスできません。管理者でログインしてください。");
          session.setAttribute("errormessage", error);
          response.sendRedirect("/YasuiBasic/index");
          return;
      }
      chain.doFilter(req, res);
      return;
    }

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
    // TODO 自動生成されたメソッド・スタブ

  }

}
