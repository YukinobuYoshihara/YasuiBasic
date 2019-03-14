package jp.recruit.misc;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ErrorMessageHandler {
	ArrayList<String> errorMessages = new ArrayList<String>();
	HttpServletRequest request;
	public ErrorMessageHandler(ArrayList<String> errorMessages,
			HttpServletRequest request) {
		super();
		this.errorMessages = errorMessages;
		this.request=request;
	}

	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(ArrayList<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	public void addErrorMessage(String errorMessage){
		this.errorMessages.add(errorMessage);
	}
	public void printErrorMessages(){
		//ログファイルのファイル名を生成
		ServletContext application=request.getSession().getServletContext();
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyyMMdd");
		String path=application.getRealPath("/WEB-INF/log/" + 
				"error"+fmt.format(cal.getTime()) + ".log");
		//SingletonパターンでCustomLogオブジェクトを取得
		CustomLog log=CustomLog.getInstance();
		try{
			//画面とログに出力
			for(int i=0;i<this.errorMessages.size();i++){
				System.err.println(this.errorMessages.get(i));
				log.writeErrorLog(path, this.errorMessages.get(i));
			}
		}catch(IOException e){
			System.err.println("(ErrorMessageHandler)エラーメッセージの出力処理に失敗しました"+
					e.getLocalizedMessage());
			e.printStackTrace();
		}

	}

}
