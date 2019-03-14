package jp.recruit.servlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.recruit.bean.ItemBean;
import jp.recruit.logic.ListItemLogic;
import jp.recruit.misc.CheckUtil;

public class ChangeStockConfirm extends HttpServlet {
	private static final long serialVersionUID = -789585356886372348L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletContext sc=null;
		//エラーメッセージ用作業変数
		String message=null;

		//在庫数のリミット
		final int STOCK_LIMIT = 99999999;

		// デフォルトの転送先
		String destination = "/WEB-INF/jsp/changeStock/ChangeStockConfirm.jsp";
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		// 在庫変更用ArrayListの作成
		ArrayList<ItemBean> updatedItemList = new ArrayList<ItemBean>();
		// 作業用ArrayList
		ArrayList<ItemBean> items=null;
		updatedItemList.clear();

		// セッションの取得
		HttpSession session = request.getSession(false);

		try {
			//最新の商品一覧（在庫付き）を取得
			ListItemLogic listItemLogic = new ListItemLogic();
			items = listItemLogic.getItemList();

			//リクエストよりパラメーターのMapを取得
			Map<String,String[]> itemMap = request.getParameterMap();
			//チェック用ユーティリティクラスのインスタンス化
			CheckUtil cu = new CheckUtil();

			//リクエストパラメーターと商品リストのマージ
			for(Map.Entry<String, String[]> item:itemMap.entrySet()){
				System.out.println("Key="+item.getKey()+" :Value="+item.getValue()[0]);
				for(ItemBean updatedItem:items){
					if(updatedItem.getItemId().equals(item.getKey())){
						//エラーチェックして整数変換不可の場合はエラーメッセージを構築する
						if(cu.numberTypeCheck(item.getValue()[0], item.getKey()+"の新在庫数")){
							int newStock = Integer.parseInt(item.getValue()[0]);
							if(newStock<0){
								message = "(ChangeStockConfirm)"+updatedItem.getItemName()+"の新在庫数が不正なため、発注できません。";
								error.add(message);
								System.err.println(message);
								continue;
							}else if(updatedItem.getStock()==0){//新在庫数0はスキップする
								continue;
							}else if(updatedItem.getStock()>STOCK_LIMIT){ //注文が制限値より大きかったらはじく
								message = "(ChangeStockConfirm)"+updatedItem.getItemName()+"の新在庫数が制限を超えているため、発注できません。";
								error.add(message);
								System.err.println(message);
								continue;
							}
							//在庫が更新されていたら、
							if(newStock!=updatedItem.getStock()){
								updatedItem.setStock(newStock);
								updatedItemList.add(updatedItem);
								System.out.println("新在庫数"+updatedItemList.size());
							}else
								continue;
						}
					}
				}
			}
			//確認用処理（本番では消すべき）
			System.err.println("作業用ArrayList確認--------");
			for(ItemBean temp:items){
				System.out.println("商品名："+temp.getItemName()+" 注文数："+temp.getStock());
			}
			//checkUtilのエラーをエラーリストに追加
			error.addAll(cu.getErrors());

			// 注文が0行だったらエラーメッセージを入力
			if (updatedItemList.size() == 0){
				System.out.println("有効在庫変更0");
				message = "(ChangeStockConfirm)在庫を変更する対象が見つからないため、変更できません。";
				error.add(message);
			}
		} catch (NamingException|SQLException e) {
			message = "(ChangeStockConfirm)"+e.getMessage()+":商品情報の取得で不具合が発生しています";
			error.add(message);
			e.printStackTrace();
		} catch (NumberFormatException e) {
			error.add("(ChangeStockConfirm)"+e.getMessage()+":注文情報の処理で不具合が発生しています");
			e.printStackTrace();
		}
		//何らかのエラーがあったらエラーメッセージを指定して差し戻し
		if(!error.isEmpty()){//異常系
			System.err.println("エラーメッセージあったよ");
			//完成したエラーメッセージ用ArrayListをrequestに格納
			request.setAttribute("errormessage",error);
			destination = "/WEB-INF/jsp/changeStock/ChangeStock.jsp";
			request.setAttribute("canChange", Boolean.valueOf(false));
			//もとのページに戻すので、単なる商品一覧のほうを戻す
			session.setAttribute("changeStock", items);
		}else{//正常系
			request.setAttribute("canChange", Boolean.valueOf(true));
			//完成した注文用ArrayListをセッションに格納（上書き）
			session.setAttribute("changeStock", updatedItemList);
		}
		// ServletContextオブジェクトを取得
		sc = this.getServletContext();
		// RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		// forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}

}
