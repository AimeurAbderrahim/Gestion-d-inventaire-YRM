package db.errors;

import java.io.IOException;

public class ReadSQLTableFileException extends IOException {
	public ReadSQLTableFileException(String msg) {
		super(msg);
	}

	public ReadSQLTableFileException(String msg , Throwable cause) {
		super(msg , cause);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
