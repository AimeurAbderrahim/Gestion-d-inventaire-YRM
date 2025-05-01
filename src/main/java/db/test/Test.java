package db.test;
import db.configuration.ConfigDatabase;
import db.errors.*;
public class Test {
	public static void main(String[] args)
	{
		ConfigDatabase db = new ConfigDatabase();
		try{
			db.getConnection();
			System.out.println("connection succesful");
			db.closeConnection();
		}catch(ConnectionFailedException error){
			System.out.println(error.getMessage());
		}catch(CloseConnectionException err){
			System.out.println(err.getMessage());
		}
		System.exit(0);
	}
}
