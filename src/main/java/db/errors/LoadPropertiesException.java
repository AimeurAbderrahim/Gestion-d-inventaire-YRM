package db.errors;

import java.io.IOException;

public class LoadPropertiesException extends IOException {
	public LoadPropertiesException (String msg) {
		super(msg);
	}

	public LoadPropertiesException (String msg , Throwable cause) {
		super(msg , cause);
	}

	@Override
	public String getMessage(){
		return "System Rayden MESSAGE : " + super.getMessage();
	}
}
