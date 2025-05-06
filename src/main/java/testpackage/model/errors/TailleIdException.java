package testpackage.model.errors;

public class TailleIdException extends Exception{
	public TailleIdException() {
	}

	public TailleIdException(String message) {
		super(message);
	}

	public TailleIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public TailleIdException(Throwable cause) {
		super(cause);
	}

	public TailleIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}
}
