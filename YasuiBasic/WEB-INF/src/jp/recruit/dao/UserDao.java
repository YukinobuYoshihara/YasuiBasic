package jp.recruit.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jp.recruit.bean.UserBean;

/**
 * @author student
 *
 */
public class UserDao extends BaseDao{
	// 
	/**
	 * nameでユーザーを1件検索　該当しなければnullをreturn
	 * @param name ユーザー名
	 * @return UserBean ユーザー情報の格納されたBean
	 * @throws SQLException
	 */
	public UserBean getUserByName(String name) throws SQLException{
		UserBean user = null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql = "SELECT user_id,name,passwd,descript,role FROM yasui_user " +
				"where yasui_user.is_delete < 1 and yasui_user.name = ?";
		try{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,name);
			rs = pstmt.executeQuery();

			if(rs.next()){
				user = new UserBean();
				user.setId(rs.getString("user_id"));
				user.setName(rs.getString("name"));
				user.setPasswd(rs.getString("passwd"));
				user.setDescript(rs.getString("descript"));
				user.setRole(rs.getString("role"));
			}
		}finally{

		}
		rs.close();
		pstmt.close();
		return user;
	}
	/**
	 * nameとpasswordでユーザーを1件検索　該当しなければnullをreturn
	 * @param name 入力されたユーザー名
	 * @param password 入力されたパスワード
	 * @return UserBean ユーザー情報の格納されたBean
	 * @throws SQLException
	 */
	public UserBean getUserByName(String name,String password) throws SQLException{
		UserBean user = null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql = "SELECT user_id,name,passwd,descript,role FROM yasui_user " +
				"where yasui_user.is_delete < 1 and yasui_user.name = ? and yasui_user.passwd = ?";
		try{
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1,name);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();

			if(rs.next()){
				user = new UserBean();
				user.setId(rs.getString("user_id"));
				user.setName(rs.getString("name"));
				user.setPasswd(rs.getString("passwd"));
				user.setDescript(rs.getString("descript"));
				user.setRole(rs.getString("role"));
			}
		}finally{
			rs.close();
			pstmt.close();
		}
		return user;
	}
}
