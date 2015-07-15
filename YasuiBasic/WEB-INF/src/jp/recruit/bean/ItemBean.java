/**
 *
 */
package jp.recruit.bean;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import jp.recruit.misc.StringValidator;

/**
 * @author student
 *
 */
public class ItemBean implements java.io.Serializable{
	public ItemBean(String itemId, String itemName, String imageUrl,
			String itemSize, int price, int stock, int order) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.imageUrl = imageUrl;
		this.itemSize = itemSize;
		this.price = price;
		this.stock = stock;
		this.order = order;
	}
	public ItemBean() {
		super();
		this.itemId = "(商品番号)";
		this.itemName = "(商品名)";
		this.imageUrl = "(画像URL)";
		this.itemSize = "（商品サイズ）";
		this.price = 0;
		this.stock = 0;
		this.order = 0;
		this.orderForDisplay = "0";
	}
	/**
	 * @return itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId セットする itemId
	 */
	public void setItemId(String itemId) {
		if(itemId!=null&&!itemId.equals("")){
			this.itemId = itemId;
		}else{
			System.err.println("(ItemBean)itemIdが空またはnullです。");
		}
	}
	private static final long serialVersionUID = -5208260759536338784L;

	private String itemId;
	private String itemName;
	private String imageUrl;
	private String itemSize;
	private int price;
	private int stock; 
	private int order;
	private int is_delete;
	private NumberFormat currency;
	//通貨表記の価格
	private String priceCurrency;
	private String stockForDisplay;
	private String orderForDisplay;

	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		if(itemName!=null&&!itemName.equals("")){
			if(StringValidator.isUTF8(itemName)){
				try{
					byte[] byteName = itemName.getBytes("ISO_8859_1");
					this.itemName = new String(byteName, "UTF-8");
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
				}
			}else{
				System.err.println("StringValidator.isUTF8(itemName)の結果：false");
			}
			this.itemName=itemName;
		}else{
			System.err.println("(ItemBean)itemNameが空またはnullです。");
		}

	}
	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}
	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		if(imageUrl!=null&&!imageUrl.equals("")){
			this.imageUrl = imageUrl;
		}else{
			System.err.println("(ItemBean)imageUrlが空またはnullです。");
		}

	}
	/**
	 * @return the itemSize
	 */
	public String getItemSize() {
		return itemSize;
	}
	/**
	 * @param itemSize the itemSize to set
	 */
	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 * ここではBean側に表示用ロジックをもたせているが、JSP側でCoreタグで判断させても良い
	 */
	public void setPrice(int price) {
		if(price>=0){
			this.price = price;
			//価格を設定した際に、通貨フォーマットの価格も設定しておく
			Locale locale = new Locale("ja","JP","YEN");
			currency = NumberFormat.getCurrencyInstance(locale);
			this.setPriceCurrency(currency.format(this.price));
		}else{
			this.price=0;
			this.setPriceCurrency(currency.format(this.price));
			System.err.println("(ItemBean)priceが負の値です。");
		}
	}
	/**
	 * @return the stock
	 */
	public int getStock() {
		return stock;
	}
	/**
	 * @param stock the stock to set
	 * ここではBean側に表示用ロジックをもたせているが、JSP側でCoreタグで判断させても良い
	 */
	public void setStock(int stock) {
		if(stock>=0){
			this.stock = stock;
			if(stock != 0){
				DecimalFormat df = new DecimalFormat("##,###,###");
				this.setStockForDisplay(df.format(stock));
			}else{
				this.setStockForDisplay("なし");
			}
		}else{
			this.setStockForDisplay("なし");			
			this.stock=0;
			System.err.println("(ItemBean)stockが負の値です。");
		}
	}
	/**
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * @param order the order to set
	 */
	public void setOrder(int order) {
		if(this.stock <= 0){
			this.setOrderForDisplay("現在注文できません");
			this.order = order;
		}else{
			this.order = order;
			// カンマ区切り表示
			DecimalFormat df = new DecimalFormat("##,###,###");
			this.setOrderForDisplay(df.format(order));
		}
	}

	public String getPriceCurrency() {
		return priceCurrency;
	}
	/**
	 * @param priceCurrency the priceCurrency to set
	 * setPrice()からだけ呼ばれるようにする
	 */
	private void setPriceCurrency(String priceCurrency) {
		this.priceCurrency = priceCurrency;
	}
	/**
	 * @return the stockForDisplay
	 */
	public String getStockForDisplay() {
		return stockForDisplay;
	}
	/**
	 * @param stockForDisplay the stockForDisplay to set
	 * setStockからだけ呼ばれるようにする
	 */
	private void setStockForDisplay(String stockForDisplay) {
		this.stockForDisplay = stockForDisplay;
	}
	/**
	 * @return the orderForDisplay
	 */
	public String getOrderForDisplay() {
		return orderForDisplay;
	}
	/**
	 * @param orderForDisplay the orderForDisplay to set
	 * ｓｅｔOrder()からだけ呼ばれるようにする
	 */
	private void setOrderForDisplay(String orderForDisplay) {
		this.orderForDisplay = orderForDisplay;
	}
	public int getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(int is_delete) {
		if(is_delete==0||is_delete==1){
			this.is_delete = is_delete;
		}else{
			System.err.println("(ItemBean)削除フラグに0または1以外の値を設定しようとしています");
		}
	}

}
