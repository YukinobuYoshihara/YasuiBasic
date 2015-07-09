package jp.recruit.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.recruit.misc.CheckUtil;
import jp.recruit.bean.ItemBean;

public class AddItemConfirm extends HttpServlet {
	private static final long serialVersionUID = 8077157819137881414L;

	protected void doPost(HttpServletRequest request , HttpServletResponse response)
			throws ServletException,IOException {
		ServletContext sc=null;
		String destination=null;
		//デフォルトの転送先;
		destination = "/WEB-INF/jsp/addItem/AddItemConfirm.jsp";
		//エラーメッセージ処理クラスのインスタンス化
		ArrayList<String> error = new ArrayList<String>();
		//セッションの取得（なければ作成）
		HttpSession session = request.getSession(false);
		session.removeAttribute("errormessage");
		//セッションに残ったフラグを消去
		session.removeAttribute("canAdd");

		//商品IDの取得
		String item_id = request.getParameter("itemid");
		if(item_id==null||item_id.isEmpty()){
			error.add("（AddItemConfirmServlet）商品IDが入力されていません");
		}
		System.out.println("item_id="+item_id);
		//商品名の取得
		String name = request.getParameter("item_name");
		if(name==null||name.isEmpty()){
			error.add("（AddItemConfirmServlet）商品名が入力されていません");
		}
		System.out.println("name="+name);
		String url = request.getParameter("imgurl");
		if(url==null||url.isEmpty()){
			error.add("（AddItemConfirmServlet）商品画像URLが入力されていませんでした");
		}
		System.out.println("url="+url);
		String size = request.getParameter("item_size");
		if(size==null||size.isEmpty()){
			error.add("（AddItemConfirmServlet）商品サイズが入力されていません");
		}
		System.out.println("size="+size);
		String priceString = request.getParameter("price");
		if(priceString==null||priceString.isEmpty()){
			error.add("（AddItemConfirmServlet）商品価格が入力されていません");
		}
		System.out.println("priceString="+priceString);
		String stockString = request.getParameter("stock");
		if(stockString==null||stockString.isEmpty()){
			error.add("（AddItemConfirmServlet）商品入庫数が入力されていません");
		}
		System.out.println("stockString="+stockString);
		
		//入力漏れエラーが記録されているとNullぽになるので、エラーサイズが0の場合だけ形式チェックを行う
		if(error.size()==0){
			//数値処理用一時変数の宣言
			int price=0;
			int stock=0;
			//入力データのチェック
			//価格と在庫が数値型かどうかのチェック
			CheckUtil cu = new CheckUtil();
			if(cu.numberTypeCheck(priceString, "価格")||cu.numberTypeCheck(stockString, "在庫")){
				try{
					price=Integer.parseInt(priceString);
					stock=Integer.parseInt(stockString);
					System.out.println("price=");
				}catch(NumberFormatException e){
					//追加不可フラグを立てて処理続行する運用
					error.add("（AddItemConfirmServlet）入力された価格または在庫の形式が不正です。数値で入力してください：");
				}
			}
			//商品名の重複チェック
			String sql="SELECT * FROM ITEM WHERE item_name = ?";
			if(!cu.duplicateCheck(name,sql,"商品名")){
				error.add("（AddItemConfirmServlet）入力された商品名"+name+"が重複しています");
			}
			//商品名のパターンチェック
			if(!cu.regExCheck(name, "\\(.*\\)|（.*）$", "商品名")){
				error.add("（AddItemConfirmServlet）入力された商品名の形式が不正です。例：商品名（色）");
			}

			//商品IDの重複チェック
			String sql2="SELECT * FROM ITEM WHERE item_id = ?";
			if(!cu.duplicateCheck(item_id,sql2,"商品ID")){
				error.add("（AddItemConfirmServlet）入力された商品ID"+item_id+"が重複しています");
			}
			//URLのパターンチェック
			if(!cu.regExCheck(url, "^(http|https):\\/\\/.*\\.(jpg|gif|png)$", "URL")){
				error.add("（AddItemConfirmServlet）入力されたURLの形式が不正です。例：http://ホスト名:ポート番号/コンテキストルート名/img/ファイル名");
			}
			//サイズのパターンチェック
			if(!cu.regExCheck(size, "^[0-9x]+$", "サイズ")){
				error.add("（AddItemConfirmServlet）入力された商品のサイズの形式が不正です。例：縦ｘ横ｘ高さ(cm)");
			}
			//入力データの論理チェック
			//価格や在庫数がマイナスだった場合canAddフラグをfalseに設定
			if(price < 0 || price > 99999999){
				error.add("（AddItemConfirmServlet）入力された価格が不正です。");
			}
			if(stock < 0 || stock > 99999999){
				error.add("（AddItemConfirmServlet）入力された在庫数が不正です。");
			}
			//エラーがなかったら
			if(error.isEmpty()){
				System.out.println("正常の場合");
				//正常を確認できた場合だけ、新規商品用のデータをBeanに収める
				ItemBean newItem = new ItemBean();
				System.out.println("price="+price);
				System.out.println("stock="+stock);
				newItem.setItemNum(item_id);
				newItem.setItemName(name);
				newItem.setImageUrl(url);
				newItem.setItemSize(size);
				newItem.setPrice(price);
				newItem.setStock(stock);
				session.setAttribute("canAdd",true);
				session.setAttribute("newItem",newItem);
			}
		}
		if(!error.isEmpty()){
			System.out.println("エラーのあった場合");
			destination="/AddItem";
			session.setAttribute("canAdd", Boolean.valueOf(false));
			//完成したエラーメッセージ用ArrayListをセッションに格納
			session.setAttribute("errormessage",error);
		}

		//ServletContextオブジェクトを取得
		sc = this.getServletContext();
		//RequestDispatcherオブジェクトを取得
		RequestDispatcher rd = sc.getRequestDispatcher(destination);
		//forwardメソッドで、処理をreceive.jspに転送
		rd.forward(request, response);
	}
}
