package jp.recruit.servlet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.recruit.bean.*;
import jp.recruit.logic.ListItemLogic;
import jp.recruit.misc.CheckUtil;

public class PurchaseConfirm extends YasuiServlet {
	private static final long serialVersionUID = 7612802445832537582L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		ServletContext sc=null;
		String destination=null;
		//デフォルトの遷移先
		destination = "/WEB-INF/jsp/purchase/confirm.jsp";

		// セッションの取得（なければ作成）
		HttpSession session = req.getSession(false);

		// 注文用ArrayListの作成
		ArrayList<ItemBean> orderedItems = new ArrayList<ItemBean>();
		// 作業用ArrayList
		ArrayList<ItemBean> items=null;
		orderedItems.clear();
		//エラーメッセージ処理List
		ArrayList<String> error = new ArrayList<String>();
		session.removeAttribute("errormessage");
		session.removeAttribute("items");

		try {
			//最新の商品一覧（在庫付き）を取得
			ListItemLogic listItemLogic = new ListItemLogic();
			items = listItemLogic.getItemList();

			//リクエストよりパラメーターのMapを取得
			Map<String,String[]> itemMap = req.getParameterMap();
			//チェック用ユーティリティクラスのインスタンス化
			CheckUtil cu = new CheckUtil();

			//リクエストパラメーターと商品リストのマージ
			for(Map.Entry<String, String[]> item:itemMap.entrySet()){
				System.out.println("Key="+item.getKey()+" :Value="+item.getValue()[0]);
				for(ItemBean orderItem:items){
					if(orderItem.getItemNum().equals(item.getKey())){
						//エラーチェックして整数変換不可の場合はエラーメッセージを構築する
						if(cu.numberTypeCheck(item.getValue()[0], item.getKey()+"の注文数")){
							orderItem.setOrder(Integer.parseInt(item.getValue()[0]));
						}
						break;
					}
				}
			}
			System.err.println("作業用ArrayList確認--------");
			for(ItemBean temp:items){
				System.out.println("商品名："+temp.getItemName()+" 注文数："+temp.getOrder());
			}
			//checkUtilのエラーをエラーリストに追加
			error.addAll(cu.getErrors());
			//itemsの論理チェック（この段階で数値は必ず入っている
			for(ItemBean orderItem:items){
				//注文が負ならばはじく
				if(orderItem.getOrder()<0){//注文数が負だったら
					error.add("(PurchaseConfirm)"+orderItem.getItemName()+"の注文数が不正なため、発注できません。");
					System.err.println("注文数不正");
					continue;
				}else if(orderItem.getOrder()==0){//注文数0はスキップする
					continue;
				}else if(orderItem.getStock()<orderItem.getOrder()){ //注文が在庫より大きかったらはじく
					error.add("(PurchaseConfirm)"+orderItem.getItemName()+"の注文数が在庫を超えているため、発注できません。");
					System.err.println("在庫不足");
					continue;
				}else{
					orderedItems.add(orderItem);
					System.err.println("注文数"+orderedItems.size());
				}
			}


			// 注文が0行だったら注文可能フラグをfalse
			if (orderedItems.size() == 0){
				System.out.println("有効注文0");
				error.add("(PurchaseConfirm)有効な注文が確認できないため、発注できません。");
			}
		} catch (NamingException|SQLException e) {
			error.add("(PurchaseConfirm)"+e.getLocalizedMessage()+":商品情報の取得で不具合が発生しています");
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , e.getMessage());
			return;
		} catch (NumberFormatException e) {
			error.add("(PurchaseConfirm)"+e.getLocalizedMessage()+":注文情報の処理で不具合が発生しています");
			e.printStackTrace();
			res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR , e.getMessage());
			return;
		}
		//何らかのエラーがあったらエラーメッセージを指定して差し戻し
		if(!error.isEmpty()){
			System.err.println("エラーメッセージあったよ");
			session.setAttribute("errormessage",error);
			destination = "/WEB-INF/jsp/purchase/list.jsp";
			session.setAttribute("canOrder", Boolean.valueOf(false));
			//もとのページに戻すので、単なる商品一覧のほうを戻す
			session.setAttribute("items", items);
		}else{
			session.setAttribute("canOrder", Boolean.valueOf(true));
			//完成した注文用ArrayListをセッションに格納（上書き）
			session.setAttribute("items", orderedItems);
		}


		// ServletContextオブジェクトを取得
		sc = this.getServletContext();
		// RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		// forwardメソッドで、処理を転送
		rd.forward(req, res);
		return;
	}


}
