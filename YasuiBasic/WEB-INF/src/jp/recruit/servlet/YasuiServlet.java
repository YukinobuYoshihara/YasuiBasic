package jp.recruit.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext;


public abstract class YasuiServlet extends HttpServlet{
	/**
	 * エラーログを吐かせるためのFacadeクラス（今回不使用）
	 */
	private static final long serialVersionUID = 3318389635946290395L;

	public void printMessage(String message){
		ServletContext sc=null;
		sc=getServletContext();
		String mode=sc.getInitParameter("mode.debug");
		if(mode.equalsIgnoreCase("true")){
			//デバッグモードがtrueのときはデバッグメッセージを表示する
			System.out.println(message);
		}
	}
	public void printErrorMessage(String message){
		ServletContext sc=null;
		sc=getServletContext();
		String mode=sc.getInitParameter("mode.debug");
		if(mode.equalsIgnoreCase("true")){
			//デバッグモードがtrueのときはデバッグメッセージを表示する
			System.err.println(message);
		}
	}

}
