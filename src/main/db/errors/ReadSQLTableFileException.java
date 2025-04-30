package src.main.db.errors;

import java.io.IOException;

public ReadSQLTableFileException extends IOException {
	public ReadSQLTableFileException(String msg) {
		super(msg);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
