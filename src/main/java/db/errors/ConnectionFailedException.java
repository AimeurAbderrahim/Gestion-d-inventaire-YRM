package db.errors;

import java.sql.SQLException;

public class ConnectionFailedException extends SQLException {
	public ConnectionFailedException(String msg) {
		super(msg);
	}

	public ConnectionFailedException(String msg , Throwable cause) {
		super(msg , cause);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
