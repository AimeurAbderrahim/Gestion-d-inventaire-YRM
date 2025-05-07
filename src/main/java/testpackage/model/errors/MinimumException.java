package testpackage.model.errors;

public class MinimumException extends Exception{
	//public Minimum() {
	//}

	public MinimumException(String message) {
		super(message);
	}

	public MinimumException(String message, Throwable cause) {
		super(message, cause);
	}

	public MinimumException(Throwable cause) {
		super(cause);
	}

	public MinimumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
