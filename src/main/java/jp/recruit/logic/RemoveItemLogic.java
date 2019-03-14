package jp.recruit.logic;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import jp.recruit.bean.ItemBean;
import jp.recruit.dao.ItemDao;

public class RemoveItemLogic extends AbstractLogic {

	public RemoveItemLogic() {
		super();
	}
	public int removeItem(ArrayList<ItemBean> targetItems)throws SQLException,NamingException{
		int result=0;
		ItemDao dao = new ItemDao();
		//エラーコードにより障害の根本原因を格納する文字列
		String rootcause=null;
		System.out.println("削除ロジック呼び出し");
		try{
			dao.getConnection();
			result = dao.removeItemList(targetItems);
			if(result<0){
				switch(result){
				case 0:
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
