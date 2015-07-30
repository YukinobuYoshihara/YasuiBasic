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

public class RemoveItemConfirm extends HttpServlet {
	private static final long serialVersionUID = 3842549697179632660L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletContext sc=null;
		String destination="/WEB-INF/jsp/removeItem/RemoveItemConfirm.jsp";
		String message=null;

		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//削除商品用ArrayListの作成
		ArrayList<ItemBean> targetItems = new ArrayList<ItemBean>();
		// セッションの取得
		HttpSession session = request.getSession(false);

		// 商品一覧のArrayList
		ArrayList<ItemBean> items = null;

		//ロジッククラスのインスタンス作成
		ListItemLogic listItemLogic = new ListItemLogic();
		//最新の商品一覧（在庫付き）を取得
		try{
			items = listItemLogic.getItemList();
			//リクエストよりパラメーターのMapを取得
			Map<String,String[]> itemMap = request.getParameterMap();
			//チェック用ユーティリティクラスのインスタンス化
			CheckUtil cu = new CheckUtil();

			//リクエストパラメーターと商品リストのマージ
			for(Map.Entry<String, String[]> mappedItem:itemMap.entrySet()){
				System.out.println("Key="+mappedItem.getKey()+" :Value="+mappedItem.getValue()[0]);
				for(ItemBean item:items){
					if(item.getItemId().equals(mappedItem.getKey())){
						targetItems.add(item);
						break;
					}
				}
			}
			System.err.println("作業用ArrayList確認--------");
			for(ItemBean temp:targetItems){
				System.out.println("削除対象の商品名："+temp.getItemName());
			}

			//checkUtilのエラーをエラーリストに追加
			if(cu.hasErrors()){
				error.addAll(cu.getErrors());
			}
			// 注文が0行だったら注文可能フラグをfalse
			if (targetItems.size() == 0){
				System.out.println("有効な削除指定0");
				message="(RemoveItemConfirm)有効な削除指定が確認できないため、削除できません。";
				error.add(message);
			}
		} catch (NamingException|SQLException e) {
			message="(RemoveItemConfirm)"+e.getMessage()+":商品情報の取得で不具合が発生しています";
			e.printStackTrace();
			error.add(message);
		} catch (NumberFormatException e) {
			message="(RemoveItemConfirm)"+e.getMessage()+":注文情報の処理で不具合が発生しています";
			e.printStackTrace();
			error.add(message);
		}

		//エラーの有無で正常系と異常系を分ける
		if(!error.isEmpty()){//異常系
			System.err.println("エラーを発見したよ:"+error.size());
			//完成したエラーメッセージ用ArrayListをセッションに格納
			request.setAttribute("errormessage",error);
			//エラーがあったので削除の初期画面に差し戻し
			destination = "/RemoveItem";
			request.setAttribute("canRemove", Boolean.valueOf(false));
		}else{//正常系
			request.setAttribute("canRemove", Boolean.valueOf(true));
			//確認済削除対象商品情報ArrayListをセッションに格納
			session.setAttribute("targetItems", targetItems);
			// ServletContextオブジェクトを取得
			sc = this.getServletContext();
			// RequestDispatcherオブジェクトを取得
			RequestDispatcher rd = sc.getRequestDispatcher(destination);
			// forwardメソッドで、処理をreceive.jspに転送
			rd.forward(request, response);
		}
	}
}
