package jp.recruit.logic;

import java.sql.SQLException;

import javax.naming.NamingException;

import jp.recruit.bean.UserBean;
import jp.recruit.dao.UserDao;

public class LoginCheckLogic extends AbstractLogic {

	public LoginCheckLogic() {

	}
	/**
	 * DAOを呼び出してユーザーをユーザー名とパスワードで検索する
	 * @param username
	 * @param password
	 * @return 発見できたらそのユーザーのUserBean、できなかったらnull
	 * @throws SQLException
	 * @throws NamingException
	 */
	public UserBean authCheck(String username,String password)throws SQLException,NamingException{
		UserDao userDao = new UserDao();
		UserBean userBean = null;
		//ユーザー名とパスワードが空でなかったらログインチェック
		if((username!=null&&!username.isEmpty())&&(password!=null&&!password.isEmpty())){
			try{
				//データベースに接続して、ユーザー情報を取得
				userDao.getConnection();
				userBean=userDao.getUserByName(username,password);
				//ユーザー情報が取得でき、パスワードが一致したらログイン成功
				if(userBean!=null&&userBean.getPasswd().equals(password)){
					return userBean;
				}
			}finally{
				//データベースと切断
				userDao.closeConnection();
			}
		}
		//ここまで来たらログインは失敗
		return null;
	}
}
