package jp.recruit.exception;
/**
 * 注文処理の際のエラーを処理するために使用する例外クラス
 * @author UU074821
 *
 */
public class ProcessOrderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3004505807929286575L;

	public ProcessOrderException() {
		
	}

	public ProcessOrderException(String arg0) {
		super(arg0);
		
	}

	public ProcessOrderException(Throwable arg0) {
		super(arg0);
		
	}

	public ProcessOrderException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
	}

	public ProcessOrderException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		
	}

}
