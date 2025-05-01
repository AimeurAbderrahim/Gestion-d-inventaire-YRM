package src.main.db.errors;

import java.sql.SQLException;

public class ExecuteStatementException extends SQLException {
	public ExecuteStatementException(String msg) {
		super(msg);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
