package src.main.db.errors;

import java.sql.SQLException;

public ConnectionFailedException extends SQLException {
	public ConnectionFailedException(String msg) {
		super(msg);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
