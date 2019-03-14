package jp.recruit.logic;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.naming.NamingException;

import jp.recruit.bean.ItemBean;
import jp.recruit.dao.ItemDao;

public class ListItemLogic  extends AbstractLogic {

	public ListItemLogic() {
		super();
	}
	public ArrayList<ItemBean> getItemList()throws SQLException,NamingException, IOException{
		ArrayList<ItemBean> itemList=null;
		//商品一覧を取得して、ArrayList作成
		ItemDao itemDao = new ItemDao();
		try{
			itemDao.getConnection();
			itemList=itemDao.getAllItemsWithStock();
		}finally{
			itemDao.closeConnection();
		}
		//itemListをソート
		Collections.sort(itemList, new Comparator<ItemBean>(){
			@Override
			public int compare(ItemBean p1, ItemBean p2) {
				//String同士のソートをするときはcompareTo()を使用する
				return p1.getItemId().compareTo(p2.getItemId());
			}
		});
		return itemList;
	}
	public ArrayList<ItemBean> getItemList(ArrayList<ItemBean> backwardList) throws SQLException,NamingException, IOException{
		ArrayList<ItemBean> itemList=null;
		
		//商品一覧の取得。、。、
		itemList=this.getItemList();

		//backwardListをソート
		Collections.sort(backwardList, new Comparator<ItemBean>(){
			@Override
			public int compare(ItemBean p1, ItemBean p2) {
				//String同士のソートをするときはcompareTo()を使用する
				return p1.getItemId().compareTo(p2.getItemId());
			}
		});

		//itemListとBackwardListをマージ
		for(ItemBean item:itemList){
			for(ItemBean backwardItem:backwardList){
				if(item.getItemId().equals(backwardItem.getItemId())){
					item.setOrder(backwardItem.getOrder());
				}
			}
		}

		return itemList;
	}
}
