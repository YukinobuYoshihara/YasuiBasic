package jp.recruit.logic;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import jp.recruit.bean.ItemBean;
import jp.recruit.dao.ItemDao;

public class StockUpdateLogic extends AbstractLogic {

	public StockUpdateLogic() {
		super();
	}


	public int updateStock(ArrayList<ItemBean> orderItems,String username)throws SQLException,NamingException{
		ItemDao dao = new ItemDao();
		//処理の結果を受け取る変数
		int result=0;
		try{
			dao.getConnection();
			//注文により在庫更新処理の実行
			result = dao.updateStock(orderItems);
			if(result<0)
				_errs.add("(StockUpdateLogic.java)在庫の更新失敗:エラーコード："+result);
			else if(result ==0){
				_errs.add("(StockUpdateLogic.java)更新失敗");
			}
			dao.insertOrder(username, orderItems);
		}finally{
			dao.closeConnection();
		}
		return result;
	}

}
