package db.errors;

import java.sql.SQLException;

public class CloseConnectionException extends SQLException {
	public CloseConnectionException(String msg) {
		super(msg);
	}

	public CloseConnectionException(String msg , Throwable cause) {
		super(msg , cause);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
