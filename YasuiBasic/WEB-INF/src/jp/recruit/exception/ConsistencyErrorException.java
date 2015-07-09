package jp.recruit.exception;

public class ConsistencyErrorException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8740703924628969112L;

	public ConsistencyErrorException() {
		
	}

	public ConsistencyErrorException(String message) {
		super(message);
		
	}

	public ConsistencyErrorException(Throwable cause) {
		super(cause);
		
	}

	public ConsistencyErrorException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public ConsistencyErrorException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
	}

}
