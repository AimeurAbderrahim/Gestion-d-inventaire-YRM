package db.test;

import java.util.UUID;

// test 1
import db.configuration.ConfigDatabase;
// test 2
import db.java.EntityCoreDatabase;
import db.java.FournisseurDatabase;
import db.errors.*;
import testpackage.model.core.Fournisseur;
import testpackage.model.errors.NotNullException;
public class Test {
	public static void main(String[] args)
	{

		// UUID uuid = UUID.randomUUID();
		// System.out.println(uuid.toString());
		// System.exit(0);

		// I)- test configuration of database
		ConfigDatabase db = null;
		try{
			db = new ConfigDatabase();
			db.getConnection();
			System.out.println("connection succesful");
			db.closeConnection();
		}catch(ConnectionFailedException error){
			System.out.println(error.getMessage());
			System.exit(0);
		}catch(CloseConnectionException err){
			System.out.println(err.getMessage());
			System.exit(0);
		}catch(LoadPropertiesException err){
			System.out.println(err.getMessage());
			System.exit(0);
		}
		// II)- test operations on tables
		try {
			// Create the database access object
			FournisseurDatabase fournisseurDB = new FournisseurDatabase(db , null , null);
			Fournisseur f = fournisseurDB.findById("F101");
			if(f != null){
				System.out.println("Found supplier: " + f.getNom_f());
				System.out.println("Email: " + f.getMail_f());
			}else{
				System.out.println("not found");
			}

			// id generated
			fournisseurDB.add(new Fournisseur( "testname", "1010 Route des Importateurs, Annaba", "0234958234", "test@test.dz", "NIF10456789" , "NIS10456789" , "IA10456789" , "RC10456789"));
			System.out.println("added");
			// Or with custom ID column name
			// FournisseurDatabase fournisseurDB = new FournisseurDatabase("custom_id_column");
		} catch (ConnectionFailedException e) {
			System.err.println("Failed to connect to database: " + e.getMessage());
		} catch(OperationFailedException e){
			System.err.println("Failed to find by id: " + e.getMessage());
		}catch(NotNullException e){
			System.err.println("Failed null: " + e.getMessage());
		}
		System.exit(0);
	}
}
