package db.test;

import java.util.UUID;

// test 1
import db.configuration.ConfigDatabase;
// test 2
import db.java.EntityCoreDatabase;
import db.java.FournisseurDatabase;
import db.java.EmplacementDatabase;
import db.errors.*;
import testpackage.model.core.Fournisseur;
import testpackage.model.core.Emplacement;
import testpackage.model.enumeration.*;
import testpackage.model.errors.NotNullException;

import db.utils.CreateAccount;
import db.utils.AuthunticationVerification;
import testpackage.model.utils.ConvertEnum;

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
			try {	
				FournisseurDatabase fournisseurDB = new FournisseurDatabase(db , null , null);
				Fournisseur f = fournisseurDB.findById("F101");
				if(f != null){
					System.out.println("Found supplier: " + f.getNom());
					System.out.println("Email: " + f.getEmail());
				}else{
					System.out.println("not found");
				}
				fournisseurDB.add(new Fournisseur("testname", "1010 Route des Importateurs, Annaba", "0234958234", "test@test.dz", "NIF10456792" , "NIS10456792" , "RC10456792"));

			} catch (Exception e) {
				System.err.println("Failed to connect to database: " + e.getMessage());
			}
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
		// try {
		// 	// 1)- fournisseur
		// 	// Create the database access object
		// 	// FournisseurDatabase fournisseurDB = new FournisseurDatabase(db , null , null);
		// 	// Fournisseur f = fournisseurDB.findById("F101");
		// 	// if(f != null){
		// 	// 	System.out.println("Found supplier: " + f.getNom());
		// 	// 	System.out.println("Email: " + f.getEmail());
		// 	// }else{
		// 	// 	System.out.println("not found");
		// 	// }

		// 	// id generated
		// 	// fournisseurDB.add(new Fournisseur("testname", "1010 Route des Importateurs, Annaba", "0234958234", "test@test.dz", "NIF10456792" , "NIS10456792" , "IA10456793" , "RC10456792"));
		// 	// System.out.println("added");
		// 	// Or with custom ID column name
		// 	// FournisseurDatabase fournisseurDB = new FournisseurDatabase("custom_id_column");

		// 	// 2)- emplacement
		// 	EmplacementDatabase emplacementDB = new EmplacementDatabase(db , null , null);

		// 	emplacementDB.add(new Emplacement("2221", TypeSalle.REUNION, 23.4, 3, Services.VICE_DOYENS));
		// 	emplacementDB.add(new Emplacement("2222", TypeSalle.REUNION, 23.4, 3, Services.VICE_DOYENS));
		// 	emplacementDB.add(new Emplacement("2223", TypeSalle.REUNION, 23.4, 3, Services.VICE_DOYENS));
		// 	emplacementDB.add(new Emplacement("2224", TypeSalle.REUNION, 23.4, 3, Services.VICE_DOYENS));
		// } catch (ConnectionFailedException e) {
		// 	System.err.println("Failed to connect to database: " + e.getMessage());
		// } catch(OperationFailedException e){
		// 	System.err.println("Failed to find by id: " + e.getMessage());
		// }
		// catch(NotNullException e){
		// 	System.err.println("Failed null: " + e.getMessage());
		// }
		// CreateAccount newaccount = new CreateAccount("rayden" , "rayden");
		// newaccount.login();


		AuthunticationVerification check = new AuthunticationVerification("rayden" , "rayden");
		if(check.checkAuth()){
			System.out.println("password exists");
		}
		try{
			db.closeConnection();
		}catch(CloseConnectionException error){}
		System.exit(0);
	}
}
