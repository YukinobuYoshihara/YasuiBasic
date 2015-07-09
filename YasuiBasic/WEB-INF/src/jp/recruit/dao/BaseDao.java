package jp.recruit.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

abstract public class BaseDao {
	//親クラスなのでフィールドはprotectedで作成する
	protected Connection con = null;
	protected String localName = "java:comp/env/jdbc/yasui";
	protected DataSource ds = null;
	protected Context context = null;

	// DBに接続
	public Connection getConnection() throws NamingException, SQLException{
		//コンテキストの生成
		context = new InitialContext();
		//コンテキストを検索
		ds = (DataSource)context.lookup(localName);
		// データベースへ接続
		con = ds.getConnection();
		return con;
	}

	// DBとの切断
	public void closeConnection() throws SQLException{
		if(con != null){
			con.close();
		}
	}
}
