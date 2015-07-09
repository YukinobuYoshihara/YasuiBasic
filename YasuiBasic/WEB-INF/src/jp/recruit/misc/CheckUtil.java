package jp.recruit.misc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.lang3.math.NumberUtils;

public class CheckUtil {
	private ArrayList<String> _errs=null;

	public CheckUtil() {
		this._errs = new ArrayList<String>();
	}

	public boolean hasErrors(){
		return !this._errs.isEmpty();
	}

	public void setError(String msg){
		this._errs.add(msg);
	}

	public ArrayList<String> getErrors(){
		return this._errs;
	}

	public void requiredCheck(String value,String checkTarget) {
		if(value==null || value.isEmpty()){
			this.setError(checkTarget + "は必須入力です");
		}
	}

	public boolean lengthCheck(String value,int max,String checkTarget){
		if(value!=null && !value.isEmpty()){
			if(value.length()>max){
				this.setError(checkTarget + "は" + max + "桁以下で入力してください");
				return false;
			}
		}else{
			this.setError(checkTarget+"の値がnullまたは空です。値を入力してください");
			return false;
		}
		return true;
	}

	public boolean numberTypeCheck(String value,String checkTarget){
		@SuppressWarnings("unused")
		int ival=0;
		if(value!=null && !value.isEmpty()){
			if(!NumberUtils.isNumber(value)){
				this.setError(checkTarget + "は数値で入力してください");
				return false;
			}
		}else{
			this.setError(checkTarget+"の値がnullまたは空です。値を入力してください");
			return false;
		}
		return true;
	}

	public boolean dateTypeCheck(String value,String checkTarget){
		if(value!=null && !value.isEmpty()){
			DateFormat format=new SimpleDateFormat("yyyy/MM/dd");
			format.setLenient(false);
			try{
				format.parse(value);
			}catch(ParseException e){
				this.setError(checkTarget + "は正しい日付で入力してください");
				return false;
			}
		}else{
			this.setError(checkTarget+"の値がnullまたは空です。値を入力してください");
			return false;
		}
		return true;
	}

	public boolean dateTypeCheck(String year,String month,String day,String checkTarget){
		StringBuilder dateBuilder = new StringBuilder();
		if(!NumberUtils.isDigits(year)||!NumberUtils.isDigits(year)||!NumberUtils.isDigits(year)){
			this.setError(checkTarget+"に不正な文字が入力されているか何も入力されていません。年月日は数値で入力してください");
			return false;
		}
		String date = dateBuilder.append(year).append("/").append(month).append("/").append(day).toString();

		DateFormat format=new SimpleDateFormat("yyyy/MM/dd");
		format.setLenient(false);
		try{
			format.parse(date);
		}catch(ParseException e){
			this.setError(checkTarget + "は正しい日付で入力してください");
			return false;
		}
		return true;
	}
	//範囲チェック、値が最大、最小の範囲に収まっていることをチェックする
	public boolean rangeCheck(String value,int max,int min,String checkTarget){
		if(value!=null && !value.isEmpty()){
			int val=0;
			try {
				val=Integer.parseInt(value);
			} catch (NumberFormatException e) {
				this.setError(checkTarget + "は数値で入力してください");
				return false;
			}
			if(val<min || val>max){
				this.setError(checkTarget + "は" + min + "桁以上、かつ" + max + "桁以下で入力してください");
				return false;
			}
		}else{
			this.setError(checkTarget + "が空またはNullです");
			return false;
		}
		return true;
	}
	//正規表現チェック
	//値と比較するパターン、エラー対象を指定する
	public boolean regExCheck(String value,String ptn,String checkTarget){
		if(value!=null && !value.isEmpty()){
			Pattern optn=Pattern.compile(ptn,Pattern.CASE_INSENSITIVE);
			Matcher mch=optn.matcher(value);
			if(!mch.find()){
				this.setError(checkTarget + "を正しい形式で入力してください");
				return false;
			}
		}else{
			this.setError(checkTarget + "が空またはNullです");
			return false;
		}
		return true;
	}

	public boolean compareCheck(String lowerValue,String higherValue,String lowerValueName,String higherValueName){
		if(lowerValue!=null && !lowerValue.equals("") && higherValue!=null && !higherValue.equals("")){
			if(lowerValue.compareTo(higherValue)>=0){
				this.setError(lowerValueName + "は" + higherValueName + "より小さい値を指定してください");
				return false;
			}
		}else{
			this.setError(lowerValueName+"または"+higherValueName+"が空またはnullです。");
			return false;
		}
		return true;
	}

	public boolean duplicateCheck(String checkTargetValue,String sql,String checkTargetName) {
		Connection db=null;
		PreparedStatement ps=null;
		ResultSet rs=null;
		boolean result = true;
		try {
			Context ctx=new InitialContext();
			DataSource ds=(DataSource)ctx.lookup("java:comp/env/jdbc/yasui");
			db=ds.getConnection();
			ps=db.prepareStatement(sql);
			ps.setString(1, checkTargetValue);
			rs=ps.executeQuery();
			if(rs.next()){
				this.setError("（重複チェック）"+checkTargetName + "が重複しています");
				result=false;
			}
		} catch (NamingException e) {
			this.setError("（重複チェック）データベースと接続できません");
			result=false;
			e.printStackTrace(System.err);
		} catch (SQLException e) {
			this.setError("（重複チェック）データベースへの問い合わせに失敗しました");
			result=false;
			e.printStackTrace(System.err);
		} finally {
			try {
				if(rs!=null){rs.close();}
				if(ps!=null){ps.close();}
				if(db!=null){db.close();}
			} catch (SQLException se) {
				this.setError("（重複チェック）データベースとの接続解除で障害が発生しました");
				se.printStackTrace(System.err);
			}
		}
		return result;
	}
}