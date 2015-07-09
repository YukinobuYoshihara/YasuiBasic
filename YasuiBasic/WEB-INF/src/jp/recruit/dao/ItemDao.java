package jp.recruit.dao;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import jp.recruit.bean.ItemBean;
import jp.recruit.misc.StringValidator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.naming.NamingException;

public class ItemDao extends BaseDao{
	final int FAILURE = -1;

	// 商品情報全件検索在庫付き
	public ArrayList<ItemBean> getAllItemsWithStock() throws SQLException,IOException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<ItemBean> items = new ArrayList<ItemBean>();

		String sql = "SELECT i.item_id,i.item_name,i.imgurl,i.item_size,i.price,s.stock_num " +
				"FROM YASUI.item i,YASUI.stock s WHERE i.item_id = s.item_id and i.is_delete < 1 " +
				"and s.is_delete < 1 order by i.item_id asc";
		try{
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ItemBean item = new ItemBean();
				item.setItemNum(rs.getString("item_id"));
				item.setItemName(rs.getString("item_name"));
				item.setImageUrl(rs.getString("imgurl"));
				item.setItemSize(rs.getString("item_size"));
				item.setPrice(rs.getInt("price"));
				item.setStock(rs.getInt("stock_num"));
				items.add(item);
			}
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt!=null)
				pstmt.close();
		}
		return items;
	}

	// 商品情報１件挿入 戻り値は成功したら1、一意制約違反（既に同一キーが存在していたら）の場合は-1から-3。
	public int insertItem(String cid, String name, String url, String size,
			int price, int stock) throws SQLException,UnsupportedEncodingException{
		int insertItemResult=0;
		int insertStockResult=0;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		PreparedStatement pstmt3=null;
		PreparedStatement pstmt4=null;
		ResultSet rs = null;
		final int MISCERROR=-3;
		final int NOTUNIQUEID=-1;
		final int NOTUNIQUENAME=-2;
		try{
			//商品番号の重複チェック
			String sql = "SELECT i.item_id FROM YASUI.item i where i.item_id = ?";
			pstmt1 = con.prepareStatement(sql);
			pstmt1.setString(1,cid);
			rs = pstmt1.executeQuery();
			if(rs.next()){
				return NOTUNIQUEID;
			}
			if(rs!=null)
				rs.close();
			//商品名の重複チェック
			String sql2 = "SELECT item.item_name FROM YASUI.item where item_name = ?";
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1,name);
			rs = pstmt2.executeQuery();
			if(rs.next()){
				return NOTUNIQUENAME;
			}
		}finally{
			if(rs!=null)
				rs.close();
			if(pstmt1!=null)
				pstmt1.close();
			if(pstmt2!=null)
				pstmt2.close();
		}

		if(stock < 0 ){
			return MISCERROR;//負の在庫設定はできない
		}else{
			//商品テーブル更新
			try{
				con.setAutoCommit(false);
				String sql3 = "INSERT INTO item VALUES(?,?,?,?,?)";
				pstmt3 = con.prepareStatement(sql3);
				pstmt3.setString(1,cid);
				pstmt3.setString(2,name);
				pstmt3.setString(3,url);
				pstmt3.setString(4,size);
				pstmt3.setInt(5,price);
				insertItemResult = pstmt3.executeUpdate();
				//在庫テーブル更新
				String sql4 = "INSERT INTO stock VALUES(?,?)";
				pstmt4 = con.prepareStatement(sql4);
				pstmt4.setString(1,cid);
				pstmt4.setInt(2,stock);
				insertStockResult=pstmt4.executeUpdate();
				con.commit();
				pstmt3.close();
				pstmt4.close();
			}catch(SQLException e){
				con.rollback();
				throw new SQLException(e);
			}finally{
				if(pstmt3!=null)
					pstmt3.close();
				if(pstmt4!=null)
					pstmt4.close();
			}
		}
		if(insertItemResult==insertStockResult)
			return insertItemResult;
		else
			return MISCERROR;
	}

	// 商品情報１件挿入 戻り値は成功したら1、一意制約違反（既に同一キーが存在していたら）の場合は-1から-2。その他の失敗は-3
	public int insertItemBean(ItemBean ib) throws SQLException{
		ResultSet rs = null;
		int result=0;
		final int NOTUNIQUEID=-1;
		final int NOTUNIQUENAME=-2;
		final int MISCERROR=-3;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		PreparedStatement pstmt3=null;
		PreparedStatement pstmt4=null;
		if(ib==null){
			System.err.println("itemDao#insertItemBean(ItemBean ib):引数がNull");
			return MISCERROR;//その他の失敗でリターン
		}
		String cid=ib.getItemNum();
		String name=ib.getItemName();

		System.out.println("(ItemDao.java)Beanから受け取ったname："+name);
		if(!StringValidator.isUTF8(name)){
			try{
				System.out.println("(ItemDao.java)Beanから受け取ったnameが非UTF8判定："+name);
				byte[] byteName = name.getBytes("ISO_8859_1");
				name = new String(byteName, "UTF-8");
			}catch(UnsupportedEncodingException e){
				System.err.println("(ItemDao)エンコードが不正です");
				return MISCERROR;
			}
		}
		String url=ib.getImageUrl();
		String size=ib.getItemSize();
		int price=ib.getPrice();
		int stock=ib.getStock();
		try{
			//商品番号の重複チェック
			System.out.println("(ItemDao)商品番号の重複チェック開始");
			String sql = "SELECT i.item_id FROM YASUI.item i where item_id = ?";
			pstmt1 = con.prepareStatement(sql);
			pstmt1.setString(1,cid);
			rs = pstmt1.executeQuery();
			if(rs.next()){
				System.err.println("(ItemDao)商品番号が重複しています");
				return NOTUNIQUEID;
			}
			if(rs!=null)
				rs.close();
			//商品名の重複チェック
			System.out.println("(ItemDao)商品名の重複チェック開始");
			String sql2 = "SELECT item.item_name FROM YASUI.item where item_name = ?";
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1,name);
			rs = pstmt2.executeQuery();
			if(rs.next()){
				System.err.println("(ItemDao)商品番名が重複しています");
				return NOTUNIQUENAME;
			}
		}finally{
			if(pstmt1!=null)
				pstmt1.close();
			if(pstmt2!=null)
				pstmt2.close();
			if(rs!=null)
				rs.close();
		}

		//商品テーブル更新
		System.out.println("(ItemDao)商品テーブル更新");
		//トランザクション開始

		try{
			con.setAutoCommit(false);
			String sql3 = "INSERT INTO item (item_id,item_name,imgurl,item_size,price,is_delete) VALUES(?,?,?,?,?,0)";
			pstmt3 = con.prepareStatement(sql3);
			pstmt3.setString(1,cid);
			pstmt3.setString(2,name);
			pstmt3.setString(3,url);
			pstmt3.setString(4,size);
			pstmt3.setInt(5,price);
			result = pstmt3.executeUpdate();
			System.out.println("(ItemDao)商品テーブルコミット");
			//在庫テーブル更新
			System.out.println("(ItemDao)在庫テーブル更新");
			String sql4 = "insert into stock (item_id,stock_num,is_delete) VALUES(?,?,0)";
			if(stock < 0 ){
				System.err.println("(ItemDao)在庫が負の値に設定されています");
				con.rollback();
				return MISCERROR;
			}else{
				pstmt4 = con.prepareStatement(sql4);
				pstmt4.setString(1,cid);
				pstmt4.setInt(2,stock);
				pstmt4.executeUpdate();
				pstmt4.close();
			}
			System.out.println("(ItemDao)テーブルコミット");
			con.commit();
		}catch(SQLException e){
			con.rollback();
			throw new SQLException(e);
		}finally{
			try{
				if(pstmt3!=null)
					pstmt3.close();
				if(pstmt4!=null)
					pstmt4.close();
			}catch(SQLException e){
				throw new SQLException(e);
			}
		}
		System.out.println("(ItemDao)返却予定のresult:"+result);
		return result;
	}

	// 商品情報複数件挿入 戻り値は成功したら1、一意制約違反（既に同一キーが存在していたら）の場合は-1から-2。その他の失敗は-3
	public int insertItemList(ArrayList<ItemBean> itemList) throws SQLException{
		ResultSet rs = null;
		int result=0;
		final int NOTUNIQUEID=-1;
		final int NOTUNIQUENAME=-2;
		final int MISCERROR=-3;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		PreparedStatement pstmt3=null;
		PreparedStatement pstmt4=null;
		for(ItemBean ib : itemList){
			String cid=ib.getItemNum();
			String name=ib.getItemName();

			String url=ib.getImageUrl();
			String size=ib.getItemSize();
			int price=ib.getPrice();
			int stock=ib.getStock();
			try{
				//商品番号の重複チェック
				System.out.println("(ItemDao)商品番号の重複チェック開始");
				String sql = "SELECT i.item_id FROM YASUI.item i where i.item_id = ?";
				pstmt1 = con.prepareStatement(sql);
				pstmt1.setString(1,cid);
				rs = pstmt1.executeQuery();
				if(rs.next()){
					System.err.println("(ItemDao)商品番号が重複しています");
					return NOTUNIQUEID;
				}
				if(rs!=null)
					rs.close();
				//商品名の重複チェック
				System.out.println("(ItemDao)商品名の重複チェック開始");
				String sql2 = "SELECT i.item_name FROM YASUI.item i where i.item_name = ?";
				pstmt2 = con.prepareStatement(sql2);
				pstmt2.setString(1,name);
				rs = pstmt2.executeQuery();
				if(rs.next()){
					System.err.println("(ItemDao)商品番名が重複しています");
					return NOTUNIQUENAME;
				}
				rs.close();
			}catch(SQLException e){
				con.rollback();
				throw new SQLException(e);
			}finally{
				try{
					if(pstmt1!=null)
						pstmt1.close();
					if(pstmt2!=null)
						pstmt2.close();
					if(rs!=null)
						rs.close();
				}catch(SQLException e){
					throw new SQLException(e);
				}
			}

			//商品テーブル更新
			System.out.println("(ItemDao)商品テーブル更新");
			//トランザクション開始

			try{
				con.setAutoCommit(false);
				String sql3 = "INSERT INTO item (item_id,item_name,imgurl,item_size,price,is_delete) VALUES(?,?,?,?,?,0)";
				pstmt3 = con.prepareStatement(sql3);
				pstmt3.setString(1,cid);
				pstmt3.setString(2,name);
				pstmt3.setString(3,url);
				pstmt3.setString(4,size);
				pstmt3.setInt(5,price);
				result = pstmt3.executeUpdate();
				System.out.println("(ItemDao)商品テーブルコミット");
				//在庫テーブル更新
				System.out.println("(ItemDao)在庫テーブル更新");
				String sql4 = "insert into stock (item_id,stock_num,is_delete) VALUES(?,?,0)";
				if(stock < 0 ){
					System.err.println("(ItemDao)在庫が負の値に設定されています");
					con.rollback();
					return MISCERROR;
				}else{
					pstmt4 = con.prepareStatement(sql4);
					pstmt4.setString(1,cid);
					pstmt4.setInt(2,stock);
					pstmt4.executeUpdate();
					pstmt4.close();
				}
				System.out.println("(ItemDao)テーブルコミット");
				con.commit();
			}catch(SQLException e){
				con.rollback();
				throw new SQLException(e);
			}finally{
				if(pstmt3!=null)
					pstmt3.close();
				if(pstmt4!=null)
					pstmt4.close();
			}
		}
		System.out.println("(ItemDao)返却予定のresult:"+result);
		return result;
	}

	//注文情報を追加できるようにする場合は発注と同時に注文情報をテーブルにインサートする
	// 注文情報１件挿入 戻り値は成功したら1、一意制約違反（既に同一キーが存在していたら）の場合は-1から-2。その他の失敗は-3
	public int insertOrder(String user_name,ArrayList<ItemBean> items)
			throws SQLException,NamingException{

		int result=0;
		int updateResult=0;
		String uid=null;
		//注文IDに添付するuidを取得（5ケタ）
		try{
			UserDao ud = new UserDao();
			ud.getConnection();
			uid=ud.getUserByName(user_name).getId();
			ud.closeConnection();
		}catch(SQLException e){
			throw new SQLException(e.getMessage());
		}catch(NamingException r){
			throw new NamingException(r.getLocalizedMessage());
		}
		//注文ID（oid）の作成
		StringBuffer sb = new StringBuffer();
		//日時情報を添付（17ケタ）
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		sb.append(sdf.format(date));
		//UIDをoidに添付（5ケタ）
		sb.append(uid);
		//競合対策で、3桁のランダムな数値をoidに添付する（3ケタ）
		DecimalFormat df = new DecimalFormat("000");
		sb.append( df.format( (int)( Math.random() * 1000 )));

		//注文IDを文字列化する（計25ケタ）
		String oid = sb.toString();
		System.out.println("(ItemDao#insertOrder)作成された注文ID:"+oid);
		PreparedStatement pstmt=null;
		try{
			con.setAutoCommit(false);
			updateResult=this.updateStock(items);
			if(updateResult==FAILURE){
				con.rollback();
				return FAILURE;
			}
			for(int i=0;i<items.size();i++){
				ItemBean tempBean = items.get(i);
				String sql = "insert into orders (oid,user_name,item_id,quantity,is_delivery)"+
						" values ( ?,?,?,?,0)";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1,oid);
				pstmt.setString(2,user_name);
				pstmt.setString(3,tempBean.getItemNum());
				pstmt.setInt(4, tempBean.getOrder());
				int tmpResult = pstmt.executeUpdate();
				if(tmpResult==0){
					con.rollback();
					System.err.println("(ItemDao#insertOrder)販売ログの挿入を失敗しました");
					break;
				}else{
					result+=tmpResult;
				}
			}
			pstmt.close();
			if(updateResult==result){
				//
				this.con.commit();
			}else{
				this.con.rollback();
				return FAILURE;
			}
		}catch(SQLException e){
			con.rollback();
			throw new SQLException(e);
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
			}catch(SQLException e){
				throw new SQLException(e);
			}
		}
		return result;
	}

	//在庫のアップデート　成功すると1、失敗すると-1
	public int updateStock(ArrayList<ItemBean> order)throws SQLException{
		String locksql = "select stock.stock_num from stock where item_id=? for update";
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			con.setAutoCommit(false);
			for(int i=0; i<order.size(); i++){
				ItemBean ib = order.get(i);
				int newStock = ib.getStock()-ib.getOrder();
				//排他制御対応可能なPreparedStatementオブジェクトの作成
				pstmt = con.prepareStatement(locksql,ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
				pstmt.setString(1,ib.getItemNum());
				rs = pstmt.executeQuery();
				if(rs.next()){
					rs.updateInt("stock_num", newStock);
					rs.updateRow();
					result++;
				}else{
					result=FAILURE;
				}
				rs.close();
				pstmt.close();
			}
			con.commit();
		}catch(SQLException e){
			con.rollback();
			throw new SQLException(e);
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				if(rs!=null){
					rs.close();
				}
			}catch(SQLException e){
				throw new SQLException(e);
			}
		}
		return result;
	}

	//* 	削除フラグを設定した場合の削除メソッド。削除フラグカラムの値を変えるだけ
	// 商品情報削除　戻り値は削除した行数(削除フラグを立てる）
	public int deleteItemByItemId(String id) throws SQLException{
		String sql1 = "UPDATE YASUI.item SET item.is_delete = 1 where id = ?";
		String sql2 = "UPDATE YASUI.stock SET item.is_delete = 1 where id = ?";
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		int result=0;
		try{
			con.setAutoCommit(false);
			pstmt1 = con.prepareStatement(sql1);
			pstmt1.setString(1,id);
			result = pstmt1.executeUpdate();
			pstmt1.close();
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1,id);
			pstmt2.executeUpdate();
			pstmt2.close();
			con.commit();
		}catch(SQLException e){
			con.rollback();
			throw new SQLException(e);
		}finally{
			try{
				if(pstmt1!=null){
					pstmt1.close();
				}
				if(pstmt2!=null){
					pstmt2.close();
				}
			}catch(SQLException e){
				throw new SQLException(e);
			}
		}
		return result;
	}



	// 商品情報完全削除:戻り値は削除した行数（テーブルからも削除）
	public int removeItemByItemId(String id) throws SQLException{
		String sql1 = "delete from item where item_id= ?";
		String sql2 = "delete from stock where item_id= ?";
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		int result=0;

		try{
			con.setAutoCommit(false);
			pstmt1 = con.prepareStatement(sql1);
			pstmt1.setString(1,id);
			result = pstmt1.executeUpdate();
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1,id);
			pstmt2.executeUpdate();
			con.commit();
			pstmt1.close();
			pstmt2.close();
		}catch(SQLException e){
			con.rollback();
			throw new SQLException(e);
		}finally{
			if(pstmt1!=null){
				pstmt1.close();
			}
			if(pstmt2!=null){
				pstmt2.close();
			}
		}
		return result;
	}

	// 商品情報削除:戻り値は削除した行数
	public int removeItemList(ArrayList<ItemBean> targetItems) throws SQLException{
		String sql1 = "UPDATE YASUI.item SET item.is_delete = 1 where item_id = ?";
		String sql2 = "UPDATE YASUI.stock SET stock.is_delete = 1 where item_id = ?";
		PreparedStatement pstmt0=null;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		int result=0;

		try{
			con.setAutoCommit(false);
			String sqlLock = "select item_id,item_name,imgurl,item_size,price,is_delete from item where item_id = ? for update";
			pstmt0 = con.prepareStatement(sqlLock);
			pstmt1 = con.prepareStatement(sql1);
			pstmt2 = con.prepareStatement(sql2);
			for(ItemBean targetItem:targetItems){
				pstmt0.setString(1, targetItem.getItemNum());
				pstmt0.executeQuery();
				pstmt1.setString(1,targetItem.getItemNum());
				int itemResult = pstmt1.executeUpdate();
				pstmt2.setString(1,targetItem.getItemNum());
				int stockResult = pstmt2.executeUpdate();
				//両方アップデート成功したら
				if(itemResult==stockResult){
					result+=itemResult;
					System.err.println(targetItem.getItemName()+"の削除成功");
				}
			}
			con.commit();
		}catch(SQLException e){
			System.err.println("removeItemList:SQLException");
			con.rollback();
			throw new SQLException(e);
		}finally{
			if(pstmt1!=null){
				pstmt1.close();
			}
			if(pstmt2!=null){
				pstmt2.close();
			}
		}
		return result;
	}


	//追加すべき商品のIDを取得する　
	public String getNextItemId()throws SQLException,NumberFormatException{
		String sql = "select max(item_id) from item";
		String nextId = null;
		ResultSet rs=null;
		String temp = "99999"; //ダミー
		int currentMax=-1;//ダミー
		PreparedStatement pstmt=null;
		try{
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next())
				temp = rs.getString("max(item_id)");
			rs.close();
			pstmt.close();
		}finally{
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				pstmt.close();
			}
		}
		currentMax = Integer.parseInt(temp);
		DecimalFormat df = new DecimalFormat("00000");
		nextId = df.format(currentMax+1);
		return nextId;
	}
}
