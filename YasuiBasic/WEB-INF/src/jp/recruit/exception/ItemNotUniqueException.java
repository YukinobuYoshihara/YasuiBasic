/**
 * 
 */
package jp.recruit.exception;

/**
 * 商品IDや商品名などユニーク制約に違反する商品があった場合の例外
 * @author UU074821
 *
 */
public class ItemNotUniqueException extends Exception {

	String violatedField="";
	String violatedName="";
	/**
	 * 
	 */
	private static final long serialVersionUID = -5392820834965848197L;

	/**
	 * 
	 */
	public ItemNotUniqueException() {
		
	}
	
	public ItemNotUniqueException(String violatedField,String violatedName) {
		super("商品の"+violatedField+"の値："+violatedName+"が重複しています。");
	}

	/**
	 * @param arg0
	 */
	public ItemNotUniqueException(String arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 */
	public ItemNotUniqueException(Throwable arg0) {
		super(arg0);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ItemNotUniqueException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public ItemNotUniqueException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		
	}
	
	

	/**
	 * @return violatedField
	 */
	public String getViolatedField() {
		return violatedField;
	}

	/**
	 * @param violatedField セットする violatedField
	 */
	public void setViolatedField(String violatedField) {
		this.violatedField = violatedField;
	}

	/**
	 * @return violatedName
	 */
	public String getViolatedName() {
		return violatedName;
	}

	/**
	 * @param violatedName セットする violatedName
	 */
	public void setViolatedName(String violatedName) {
		this.violatedName = violatedName;
	}
	
	

}
