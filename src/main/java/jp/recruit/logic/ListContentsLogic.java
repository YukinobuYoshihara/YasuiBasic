package jp.recruit.logic;

import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.NamingException;

import jp.recruit.bean.ContentsBean;
import jp.recruit.dao.ContentsDao;

public class ListContentsLogic extends AbstractLogic  {

	public ListContentsLogic() {
		super();
	}
	public HashMap<String,ContentsBean> getContentsMap()throws SQLException,NamingException{
		ContentsDao contentsDao = new ContentsDao();
		HashMap<String,ContentsBean> contentsMap = new HashMap<String,ContentsBean>();
		//ユーザー名とパスワードが空でなかったらログインチェック
			try{
				//データベースに接続して、ユーザー情報を取得
				contentsDao.getConnection();
				contentsMap=contentsDao.getAllContents();
				if(contentsMap.isEmpty()){
					contentsMap = null;
				}
			}finally{
				//データベースと切断
				contentsDao.closeConnection();
			}
		return contentsMap;
	}
}
