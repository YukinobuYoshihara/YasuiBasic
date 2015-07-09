package jp.recruit.misc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

public class CustomLog implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = -5905248572253471531L;
	//staticなクラス変数
	private static CustomLog _log=null;
	private CustomLog() {}
	//staticなクラス変数がヌルのときだけnewする→一度newされたら新規にnewされない
	public static CustomLog getInstance(){
		if(_log==null){
			_log=new CustomLog();
		}
		return _log;
	}
	//メソッドが常に同期化されるように宣言し、同時処理のリスクを回避
	public synchronized void writeLog(String path, HttpServletRequest request)
		throws IOException {
		//文字列の編集（追加）を行うため、StringBufferを使用
		StringBuffer sb=new StringBuffer();
		//文字出力用のストリームを作成
		BufferedWriter bw=new BufferedWriter(new FileWriter(path,true),10);
		//ログデータを作成
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(fmt.format(cal.getTime()));
		sb.append(",");
		sb.append(request.getServletPath());
		sb.append(",");
		sb.append(request.getHeader("referer"));
		sb.append(",");
		sb.append(request.getLocale());
		sb.append(",");
		sb.append(request.getHeader("user-agent"));
		//Stringに変換し書き込み
		bw.write(sb.toString());
		bw.newLine();
		bw.close();
	}
	//メソッドが常に同期化されるように宣言し、同時処理のリスクを回避
	public synchronized void writeErrorLog(String path, String errorMessage)
		throws IOException {
		//文字列の編集（追加）を行うため、StringBufferを使用
		StringBuffer sb=new StringBuffer();
		//文字出力用のストリームを作成
		BufferedWriter bw=new BufferedWriter(new FileWriter(path,true),10);
		//ログデータを作成
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append(fmt.format(cal.getTime()));
		sb.append(",");
		sb.append(errorMessage);
		//Stringに変換し書き込み
		bw.write(sb.toString());
		bw.newLine();
		bw.close();
	}
	public ArrayList<String> readLog(String path) throws IOException{
		ArrayList<String> list=null;
		File fl=new File(path);
		if(fl.exists()){
			BufferedReader br=new BufferedReader(
				new FileReader(fl),10);
			list=new ArrayList<String>();
			while(br.ready()){
				list.add(br.readLine());
			}
			br.close();
		}
		return list;
	}
}
