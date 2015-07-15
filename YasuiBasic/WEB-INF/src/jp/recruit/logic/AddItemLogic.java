package jp.recruit.logic;

import java.sql.SQLException;

import javax.naming.NamingException;

import jp.recruit.bean.ItemBean;
import jp.recruit.dao.ItemDao;
import jp.recruit.exception.ConsistencyErrorException;
import jp.recruit.exception.ItemNotUniqueException;
import jp.recruit.exception.ValidationErrorException;

public class AddItemLogic extends AbstractLogic {

	public AddItemLogic() {
		super();
	}
	public int insertItem(ItemBean newItem) throws SQLException,NamingException, ItemNotUniqueException, ValidationErrorException, ConsistencyErrorException{
		ItemDao dao = new ItemDao();
		int result=0;
		try{
			dao.getConnection();
			result = dao.insertItemBean(newItem);
		}finally{
			dao.closeConnection();
		}

		return result;
	}

}
