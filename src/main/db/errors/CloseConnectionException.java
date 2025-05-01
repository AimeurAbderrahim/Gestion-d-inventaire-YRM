package src.main.db.errors;

import java.sql.SQLException;

public CloseConnectionException extends SQLException {
	public CloseConnectionException() {
		super(msg);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
