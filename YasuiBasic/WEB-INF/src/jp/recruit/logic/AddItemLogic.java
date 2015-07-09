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
		//エラーコードにより障害の根本原因を格納する文字列
		String rootcause=null;
		System.out.println("AddItemLogicやでー");
		try{
			dao.getConnection();
			result = dao.insertItemBean(newItem);
			if(result<0){
				switch(result){
				case -1:
					rootcause="商品番号が重複しています。";
					break;
				case -2:
					rootcause="商品名が重複しています。";
					break;
				case -3:
					rootcause="テーブルの更新に失敗しました。";
					break;
				default:
					rootcause="不明な原因で商品追加に失敗しました。";
					break;
				}
				_errs.add("(AddItemLogic)商品の追加に失敗しました。(根本原因）："+rootcause);
			}
		}finally{
			dao.closeConnection();
		}

		return result;
	}

}
