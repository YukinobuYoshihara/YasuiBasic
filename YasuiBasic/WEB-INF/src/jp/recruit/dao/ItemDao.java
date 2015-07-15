package jp.recruit.dao;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jp.recruit.bean.ItemBean;
import jp.recruit.exception.ConsistencyErrorException;
import jp.recruit.exception.ItemNotUniqueException;
import jp.recruit.exception.ProcessOrderException;
import jp.recruit.exception.ValidationErrorException;
import jp.recruit.misc.StringValidator;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.naming.NamingException;

public class ItemDao extends BaseDao{
	final int FAILURE = -1;

	/**
	 * 商品情報全件検索在庫付き
	 * @return ArrayList<ItemBean>
	 * @throws SQLException
	 * @throws IOException
	 */
	public ArrayList<ItemBean> getAllItemsWithStock() throws SQLException,IOException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		ArrayList<ItemBean> items = new ArrayList<ItemBean>();

		String sql = "SELECT i.item_id,i.item_name,i.imgurl,i.item_size,i.price,s.stock_num " +
				"FROM item i,stock s WHERE i.item_id = s.item_id and i.is_delete = 0 " +
				"and s.is_delete = 0 order by i.item_id asc";
		try{
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()){
				ItemBean item = new ItemBean();
				item.setItemId(rs.getString("item_id"));
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


	/**
	 * 商品情報１件挿入
	 * @param itemBean
	 * @return 戻り値は成功したら1 失敗時0
	 * @throws SQLException
	 * @throws ItemNotUniqueException
	 * @throws ValidationErrorException
	 * @throws ConsistencyErrorException
	 */
	public int insertItemBean(ItemBean itemBean) throws SQLException, ItemNotUniqueException, ValidationErrorException, ConsistencyErrorException{
		ResultSet rs = null;
		int insertItemResult=0;
		int insertStockResult=0;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		PreparedStatement pstmt3=null;
		PreparedStatement pstmt4=null;
		if(itemBean==null){
			System.err.println("itemDao#insertItemBean(ItemBean itemBean):引数がNull");
			throw new ValidationErrorException("商品情報が受け取ることができませんでした");//その他の失敗でリターン
		}
		String itemId=itemBean.getItemId();
		String name=itemBean.getItemName();

		if(!StringValidator.isUTF8(name)){
			try{
				System.err.println("(ItemDao)Beanから受け取ったnameが非UTF8判定："+name);
				byte[] byteName = name.getBytes("ISO_8859_1");
				name = new String(byteName, "UTF-8");
			}catch(UnsupportedEncodingException e){
				System.err.println("(ItemDao)エンコードが不正です");
				throw new ValidationErrorException("商品情報の文字コードが不正です");
			}
		}
		String url=itemBean.getImageUrl();
		String size=itemBean.getItemSize();
		int price=itemBean.getPrice();
		int stock=itemBean.getStock();
		try{
			//商品番号の重複チェック
			System.out.println("(ItemDao)商品番号の重複チェック開始");
			String sql = "SELECT item_id FROM item where item_id = ?";
			pstmt1 = con.prepareStatement(sql);
			pstmt1.setString(1,itemId);
			rs = pstmt1.executeQuery();
			if(rs.next()){
				System.err.println("(ItemDao)商品番号が重複しています");
				throw new ItemNotUniqueException("商品ID",itemId);
			}
			if(rs!=null)
				rs.close();
			//商品名の重複チェック
			System.out.println("(ItemDao)商品名の重複チェック開始");
			String sql2 = "SELECT item_name FROM item where item_name = ?";
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1,name);
			rs = pstmt2.executeQuery();
			if(rs.next()){
				System.err.println("(ItemDao)商品番名が重複しています");
				throw new ItemNotUniqueException("商品ID",itemId);
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

		if(stock < 0 ){
			throw new ValidationErrorException("在庫数",stock);//負の在庫設定はできない
		}else{
			//商品テーブル更新
			try{
				String sql3 = "INSERT INTO item (item_id,item_name,imgurl,item_size,price,is_delete) VALUES (?,?,?,?,?,0)";
				pstmt3 = con.prepareStatement(sql3);				
				String sql4 = "INSERT INTO stock (item_id,stock_num,is_delete) VALUES (?,?,0)";
				pstmt4 = con.prepareStatement(sql4);
				//自動コミットをoff
				con.setAutoCommit(false);

				pstmt3.setString(1,itemId);
				pstmt3.setString(2,name);
				pstmt3.setString(3,url);
				pstmt3.setString(4,size);
				pstmt3.setInt(5,price);
				insertItemResult = pstmt3.executeUpdate();

				pstmt4.setString(1,itemId);
				pstmt4.setInt(2,stock);
				insertStockResult=pstmt4.executeUpdate();

				if(insertItemResult==0||insertStockResult==0||insertItemResult!=insertStockResult){
					con.rollback();
					throw new ConsistencyErrorException("商品情報と在庫情報の登録の整合性が取れません");
				}
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

		return insertItemResult;

	}

	/**
	 * 注文情報を追加できるようにする場合は発注と同時に注文情報をテーブルにインサートする（実際には今回は使用していない）
	 * @param user_name
	 * @param items
	 * @return 成功したら1 失敗したら-1
	 * @throws SQLException
	 * @throws NamingException
	 */
	public int insertOrder(String user_name,ArrayList<ItemBean> items)
			throws SQLException,NamingException{

		int result=0;
		int updateResult=0;
		String uid=null;

		//注文IDに添付するuidを取得（5ケタ）
		UserDao ud = new UserDao();
		try{
			ud.getConnection();
			uid=ud.getUserByName(user_name).getId();
		}finally{
			ud.closeConnection();
		}

		//注文ID（oid）の作成
		StringBuffer sb = new StringBuffer();
		//日時情報を添付（17ケタ） ※Java8必須
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		sb.append(now.format(dateTimeFormatter));

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
				pstmt.setString(3,tempBean.getItemId());
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

	/**
	 * 在庫のアップデートのみ行う（今回は使用していない）
	 * @param order
	 * @return 成功すると1、失敗すると-1
	 * @throws SQLException
	 */
	public int updateStock(ArrayList<ItemBean> order)throws SQLException{
		//行ロックをかけるSQL(select ～ for Update）
		String locksql = "select stock_num from stock where item_id=? for update";
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		try{
			con.setAutoCommit(false);
			for(ItemBean ib: order){

				int newStock = ib.getStock()-ib.getOrder();
				//排他制御対応可能なPreparedStatementオブジェクトの作成
				//ここではサンプルとして使用しているが、大規模サービスでは更新が完了するまで
				//接続しっぱなしになるため負荷が大きいので使わない
				pstmt = con.prepareStatement(locksql,ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_UPDATABLE);
				pstmt.setString(1,ib.getItemId());
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

	/**
	 * 在庫のアップデートを行い、注文情報まで挿入を行う（insertOrderとupdateStock単独版をマージ）
	 * @param order 注文のList
	 * @param user_name 注文者のユーザー名
	 * @return 成功すると1、失敗すると-1
	 * @throws SQLException
	 * @throws NamingException 
	 * @throws ConsistencyErrorException 
	 */
	public int processOrder(String user_name,ArrayList<ItemBean> order)throws SQLException, NamingException, ProcessOrderException{
		int result=0;
		int updateResult=0;
		String uid=null;
		PreparedStatement pstmt=null;
		PreparedStatement pstmtInsert=null;
		PreparedStatement pstmtUpdate=null;
		ResultSet rs=null;

		//注文IDに添付するuidを取得（5ケタ）
		UserDao ud = new UserDao();
		try{
			ud.getConnection();
			uid=ud.getUserByName(user_name).getId();
		}finally{
			ud.closeConnection();
		}

		//行ロックをかけるSQL(select ～ for Update）をStringBufferで作成
		String locksql1 = "select item_id,stock_num,is_delete from stock where item_id in (";
		String locksql2 = ") for update";
		StringBuffer locksqlBuilder = new StringBuffer().append(locksql1);
		for(int i=1;i<=order.size();i++){
			locksqlBuilder.append(" ?");
			if(i<order.size()){
				locksqlBuilder.append(", ");
			}
		}
		locksqlBuilder.append(locksql2);
		String locksql = locksqlBuilder.toString();
		System.out.println("作成できたロック用SQL"+locksql);

		//在庫更新のSQL
		String updateSql = "update stock set stock_num=? where item_id=?";
		//注文情報挿入のSQL
		String insertSql = "insert into orders (oid,item_id,user_name,quantity,is_delivery)"+
				" values ( ?,?,?,?,0)";

		try{
			//繰り返し処理を行う前に、PreparedStatementにSQLを渡してキャッシュ効果を高める
			pstmt = con.prepareStatement(locksql);//ロックをかけるSelect文
			pstmtUpdate = con.prepareStatement(updateSql);
			pstmtInsert = con.prepareStatement(insertSql);
			//自動コミットを切る
			con.setAutoCommit(false);

			//ロックをかける
			int i=1;
			for(ItemBean ib:order){
				pstmt.setString(i,ib.getItemId());
				++i;
			}

			rs = pstmt.executeQuery();
			//ロックできたかどうかチェック
			if(!rs.next()){
				System.err.println("(ItemDao#processOrder)更新対象のロックに失敗しました");
				throw new ProcessOrderException("更新対象のロックに失敗しました");
			}else{
				System.out.println("ロックできた");
			}
			//注文ID（oid）の作成
			StringBuffer sb = new StringBuffer();
			//日時情報を添付（17ケタ） ※Java8必須
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
			sb.append(now.format(dateTimeFormatter));

			//UIDをoidに添付（5ケタ）
			sb.append(uid);

			//競合対策で、3桁のランダムな数値をoidに添付する（3ケタ）
			DecimalFormat df = new DecimalFormat("000");
			sb.append( df.format( (int)( Math.random() * 1000 )));

			//注文IDを文字列化する（計25ケタ）
			String oid = sb.toString();
			System.out.println("(ItemDao#processOrder)作成された注文ID:"+oid);
			//注文のListから商品情報を1つずつ取り出して処理を行う
			for(ItemBean itemBean: order){
				//新しい在庫を計算
				int newStock = itemBean.getStock()-itemBean.getOrder();
				//ここまで来る場合通常は在庫は0以上のはずだが、競合対策でチェックする
				int tempResult1 = 0;
				if(newStock>=0){
					pstmtUpdate.setLong(1, newStock);
					pstmtUpdate.setString(2, itemBean.getItemId());
					tempResult1 = pstmtUpdate.executeUpdate();
					if(tempResult1 ==0){
						System.err.println("(ItemDao#processOrder)在庫情報の更新に失敗しました");
						throw new ProcessOrderException("在庫情報の更新に失敗しました");
					}							
					//更新できた行数の更新
					result+=tempResult1;
				}

				System.out.println("注文情報処理のブロックに入った");

				//注文情報の挿入（OIDと商品IDの複合主キーなのでOIDは1つの注文に1つで良い）
				pstmtInsert.setString(1,oid);
				pstmtInsert.setString(2,itemBean.getItemId());
				pstmtInsert.setString(3,user_name);
				pstmtInsert.setInt(4, itemBean.getOrder());
				System.err.println("oid:"+oid+"/商品ID："+itemBean.getItemId()+"/ユーザー名："+user_name+"/注文数："+itemBean.getOrder());
				int tempResult2 = pstmtInsert.executeUpdate();
				System.err.println("挿入結果："+tempResult2);
				if(tempResult2==0){
					System.err.println("(ItemDao#processOrder)販売ログの挿入を失敗しました");
					throw new ProcessOrderException("販売ログの挿入を失敗しました");
				}
				updateResult+=tempResult2;
			}

			//エラーなくループを抜けたが、更新や挿入ができていなかったらFAILURE（-1）を返す
			System.err.println("（ItemDao#processOrder)在庫情報の更新行数"+result);
			System.err.println("（ItemDao#processOrder)注文情報の挿入行数"+updateResult);
			//Exceptionはないがどちらかの処理が0で終わっていたら
			if(updateResult==0||result==0){
				con.rollback();
				return FAILURE;
			}
			//問題なくループを抜けたのでコミットする
			con.commit();
		}catch(SQLException e){
			con.rollback();
			throw new SQLException(e);
		}catch(ProcessOrderException e){
			con.rollback();
			throw new ProcessOrderException(e);
		}finally{
			try{
				if(pstmt!=null){
					pstmt.close();
				}
				if(pstmtUpdate!=null){
					pstmtUpdate.close();
				}
				if(pstmtInsert!=null){
					pstmtInsert.close();
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
	public int removeItemByItemId(String id) throws SQLException{
		String sql1 = "UPDATE item SET item.is_delete = 1 where id = ?";
		String sql2 = "UPDATE stock SET item.is_delete = 1 where id = ?";
		PreparedStatement pstmt0=null;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		int result1=0;
		int result2=0;
		String locksql = "select item_id,item_name,imgurl,item_size,price,is_delete from item where item_id =? for update";
		try{
			con.setAutoCommit(false);
			pstmt0 = con.prepareStatement(locksql);
			pstmt0.setString(1, id);
			ResultSet rs=pstmt0.executeQuery();
			pstmt1 = con.prepareStatement(sql1);
			pstmt1.setString(1,id);
			pstmt2 = con.prepareStatement(sql2);
			pstmt2.setString(1,id);

			if (rs.next()) {
				result1 = pstmt1.executeUpdate();
				result2 = pstmt2.executeUpdate();
			}
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
		if(result1==result2){
			return result1;
		}else{
			return FAILURE;
		}
	}

	// 商品情報削除:戻り値は削除した行数
	public int removeItemList(ArrayList<ItemBean> targetItems) throws SQLException{
		String sql1 = "UPDATE item SET item.is_delete = 1 where item_id = ?";
		String sql2 = "UPDATE stock SET stock.is_delete = 1 where item_id = ?";
		String locksql = null;
		PreparedStatement pstmt0=null;
		PreparedStatement pstmt1=null;
		PreparedStatement pstmt2=null;
		int result=0;
		if(targetItems.size()==1){
			locksql = "select item_id,item_name,imgurl,item_size,price,is_delete from item where item_id = ? for update";
		}else{
			//行ロックをかけるSQL(select ～ for Update）をStringBufferで作成
			String locksql1 = "select item_id,item_name,imgurl,item_size,price,is_delete from item where item_id in (";
			String locksql2 = ") for update";
			StringBuffer locksqlBuilder = new StringBuffer().append(locksql1);
			for(int i=1;i<=targetItems.size();i++){
				locksqlBuilder.append(" ?");
				if(i<targetItems.size()){
					locksqlBuilder.append(", ");
				}
			}
			locksqlBuilder.append(locksql2);
			locksql = locksqlBuilder.toString();
		}
		System.out.println("作成できたロック用SQL"+locksql);

		try{
			con.setAutoCommit(false);
			pstmt0 = con.prepareStatement(locksql);
			pstmt1 = con.prepareStatement(sql1);
			pstmt2 = con.prepareStatement(sql2);
			int i=1;
			for(ItemBean targetItem:targetItems){
				pstmt0.setString(i, targetItem.getItemId());
				i++;
			}
			ResultSet rs = pstmt0.executeQuery();
			if(rs.next()){
				for(ItemBean targetItem:targetItems){
					pstmt1.setString(1,targetItem.getItemId());
					int itemResult = pstmt1.executeUpdate();
					pstmt2.setString(1,targetItem.getItemId());
					int stockResult = pstmt2.executeUpdate();
					//両方アップデート成功したら
					if(itemResult==1&&stockResult==1){
						result+=itemResult;
						System.err.println(targetItem.getItemName()+"の削除成功");
					}else{
						con.rollback();
						return FAILURE;
					}
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


	/**
	 * 追加する商品用に商品IDを取得する
	 * 本来はSequenceによって取得をするべきだが、この時点ではmax関数によって取得する
	 * @return 文字列化した商品ID（5桁揃え）
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
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
