package jp.recruit.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import jp.recruit.misc.CustomLog;

public class CustomLogFilter implements Filter {
	public void init(FilterConfig config) throws ServletException {}
	public void doFilter(ServletRequest request,ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		//doFilterに渡されるのはHttpServletRequestではなく、ServletRequestなので明示的にキャスト
		HttpServletRequest req=(HttpServletRequest)request;
		ServletContext application=req.getSession().getServletContext();
		//ログファイルのファイル名を生成
		Calendar cal=Calendar.getInstance(); //SingletonパターンでCalendarインスタンス取得
		SimpleDateFormat fmt=new SimpleDateFormat("yyyyMMdd");
		//日付を含む形でログ・ファイルを作成
		String logname=application.getRealPath("/WEB-INF/log/" +
				fmt.format(cal.getTime()) + ".log");
		//SingletonパターンでCustomLogオブジェクトを取得
		CustomLog log=CustomLog.getInstance();
		log.writeLog(logname,req);
		chain.doFilter(request,response);
	}
	public void destroy() {}
}
