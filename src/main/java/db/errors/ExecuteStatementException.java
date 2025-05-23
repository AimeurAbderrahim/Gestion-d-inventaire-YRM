package db.errors;

import java.sql.SQLException;

public class ExecuteStatementException extends SQLException {
	public ExecuteStatementException(String msg) {
		super(msg);
	}

	public ExecuteStatementException(String msg , Throwable cause) {
		super(msg , cause);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
