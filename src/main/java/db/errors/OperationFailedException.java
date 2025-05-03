package db.errors;

import java.sql.SQLException;

public class OperationFailedException extends SQLException {
	public OperationFailedException(String msg) {
		super(msg);
	}

	public OperationFailedException(String msg , Throwable cause) {
		super(msg , cause);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
