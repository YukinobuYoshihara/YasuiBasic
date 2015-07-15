package jp.recruit.logic;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import jp.recruit.bean.ItemBean;
import jp.recruit.dao.ItemDao;
import jp.recruit.exception.ProcessOrderException;

public class StockUpdateLogic extends AbstractLogic {

	public StockUpdateLogic() {
		super();
	}


	public int updateStock(ArrayList<ItemBean> orderItems,String username)throws SQLException,NamingException, ProcessOrderException{
		ItemDao dao = new ItemDao();
		//処理の結果を受け取る変数
		int result=0;
		try{
			dao.getConnection();
			//注文により在庫更新処理の実行
			if(orderItems!=null){
				result = dao.processOrder(username, orderItems);
				if(result<0)
					_errs.add("(StockUpdateLogic)在庫の更新失敗:エラーコード："+result);
				else if(result ==0){
					_errs.add("(StockUpdateLogic)更新できる在庫情報がありませんでした");
				}
			}else{
				_errs.add("(StockUpdateLogic)更新失敗:注文情報がnullです");
			}
		}finally{
			dao.closeConnection();
		}
		return result;
	}

}
