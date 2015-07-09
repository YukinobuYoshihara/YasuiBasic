package jp.recruit.exception;

public class ValidationErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5975271534843635110L;

	public ValidationErrorException() {
		
	}
	public ValidationErrorException(String name,Object value) {
		super("入力された「"+name+"」の値："+value+"が不適切です");
	}

	public ValidationErrorException(String message) {
		super(message);
		
	}

	public ValidationErrorException(Throwable cause) {
		super(cause);
		
	}

	public ValidationErrorException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ValidationErrorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

}
