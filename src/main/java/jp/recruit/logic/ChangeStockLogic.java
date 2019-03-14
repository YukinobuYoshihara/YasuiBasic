package jp.recruit.logic;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import jp.recruit.bean.ItemBean;
import jp.recruit.dao.ItemDao;

public class ChangeStockLogic extends AbstractLogic {

	public ChangeStockLogic() {
		super();
	}
	public int updateStock(ArrayList<ItemBean> stockitems)throws SQLException,NamingException{
		ItemDao dao = new ItemDao();
		int result=0;
		//エラーコードにより障害の根本原因を格納する文字列
		String rootcause=null;

		try{
			dao.getConnection();
			result = dao.updateStock(stockitems);
			if(result<0){
				switch(result){
				case -1:
					rootcause="update文が失敗しました";
					break;
				default:
					rootcause="不明な原因で在庫の更新に失敗しました";
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
