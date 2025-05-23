/**
 * FournisseurDatabase - Data Access Layer implementation for the Fournisseur (Supplier) entity.
 * 
 * This class provides CRUD operations and database interactions specifically for the Fournisseur table,
 * extending the generic EntityCoreDatabase class with Fournisseur-specific implementations.
 */

package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;

import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;

import db.java.EntityCoreDatabase;

import testpackage.model.core.Fournisseur;

/*
 * how to use this class :
 * 1. initialisation of the class :
 * 	example :
 *		try {
 *		    // Create the database access object
 *		    FournisseurDatabase fournisseurDB = new FournisseurDatabase(null , null , null);
 *		    
 *		    // Or with custom ID column name
 *		    // FournisseurDatabase fournisseurDB = new FournisseurDatabase("custom_id_column");
 *		} catch (ConnectionFailedException e) {
 *		    System.err.println("Failed to connect to database: " + e.getMessage());
 *		} catch (LoadPropertiesException e) {
 *		    System.err.println("Failed to load database properties: " + e.getMessage());
 *		}
 *
 * 2. using CRUD operations : check Operation interface ...
 * 	2.1. add and atchoObj method 
 * 	example : 
 * 		Fournisseur newSupplier = new Fournisseur();
 * 		newSupplier.setIdF("F1001");
 * 		newSupplier.setNom("Tech Solutions Inc.");
 * 		newSupplier.setAdresse("123 Business Ave");
 * 		newSupplier.setNumeroTlph("0550123456");
 * 		newSupplier.setEmail("contact@techsolutions.dz");
 * 		newSupplier.setNIF("NIF123456789");
 * 		newSupplier.setNIS("NIS987654321");
 * 		newSupplier.setRC("RC789456123");
 * 		
 * 		try {
 * 		    fournisseurDB.add(newSupplier);
 * 		    System.out.println("Supplier added successfully!");
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Failed to add supplier: " + e.getMessage());
 * 		}
 *
 * 	2.2. findById and atchoChwiyaObjWHakId method
 * 	example : 
 * 		try {
 * 		    Fournisseur foundSupplier = fournisseurDB.findById("F1001");
 * 		    if (foundSupplier != null) {
 * 		        System.out.println("Found supplier: " + foundSupplier.getNom());
 * 		        System.out.println("Email: " + foundSupplier.getEmail());
 * 		    } else {
 * 		        System.out.println("Supplier not found");
 * 		    }
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Search failed: " + e.getMessage());
 * 		}
 *
 * 	2.3. update and bdlBdl method
 * 	example : 
 * 		try {
 * 		    // First get the existing supplier
 * 		    Fournisseur existing = fournisseurDB.findById("F1001");
 * 		    
 * 		    if (existing != null) {
 * 		        // Create updated version
 *
 *			// Assuming copy constructor
 * 		        Fournisseur updated = new Fournisseur(existing); 
 * 		        updated.setEmail("new.email@techsolutions.dz");
 * 		        updated.setAdresse("456 Corporate Blvd");
 * 		        
 * 		        // Perform update
 * 		        fournisseurDB.update(existing, updated);
 * 		        System.out.println("Supplier updated successfully");
 * 		    }
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Update failed: " + e.getMessage());
 * 		}
 * 	2.4. remove and gla3_3liya method
 * 	example : 
 *		try {
 *		    Fournisseur toDelete = fournisseurDB.findById("F1001");
 *		    if (toDelete != null) {
 *		        boolean deleted = fournisseurDB.remove(toDelete);
 *		        System.out.println(deleted ? "Deleted successfully" : "Deletion failed");
 *		    }
 *		} catch (OperationFailedException e) {
 *		    System.err.println("Deletion failed: " + e.getMessage());
 *		}
 *
 * 	2.5. findAll method
 * 	example : 
 *		try {
 *		    List<Fournisseur> allSuppliers = fournisseurDB.findAll();
 *		    System.out.println("Total suppliers: " + allSuppliers.size());
 *		    allSuppliers.forEach(supplier -> 
 *		        System.out.println(supplier.getIdF() + ": " + supplier.getNom()));
 *		} catch (OperationFailedException e) {
 *		    System.err.println("Failed to retrieve suppliers: " + e.getMessage());
 *		}
 *
 *	2.6. countAll method
 *	example :
 * 		try {
 * 		    long count = fournisseurDB.countAll();
 * 		    System.out.println("There are " + count + " suppliers in the database");
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Count operation failed: " + e.getMessage());
 * 		}
 *
 * 	2.7. 
 *
 * */

public class FournisseurDatabase extends EntityCoreDatabase<Fournisseur> {

	public FournisseurDatabase(String idCol , String tableName) throws ConnectionFailedException , LoadPropertiesException {
		super(
				(idCol == null) ? "id_f" : idCol,
				(tableName == null) ? "Fournisseur" : tableName
		);
	}

	public FournisseurDatabase(Connection conn , String idCol , String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_f" : idCol,
				(tableName == null) ? "Fournisseur" : tableName
		);
	}

	public FournisseurDatabase(ConfigDatabase conn , String idCol , String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_f" : idCol,
				(tableName == null) ? "Fournisseur" : tableName
		);
	}

	@Override
	protected String getIdValue(Fournisseur obj) {
		return obj.getId_f();
	}

	@Override
	protected int getColumnCount() {
		return 8; 
	}

	@Override
	protected int getUpdateParameterCount() {
		return 7;
	}

	@Override
	protected void setAddParameters(PreparedStatement statement, Fournisseur obj) throws SQLException {
		// TODO: instead of getting id , we will generated <line 175>
		statement.setString(1, this.generatedIdPK());
		statement.setString(2, obj.getAdresse());
		statement.setString(3, obj.getNum_tel());
		statement.setString(4, obj.getNom_f());
		statement.setString(5, obj.getMail_f());
		statement.setString(6, obj.getNIF());
		statement.setString(7, obj.getNIS());
		statement.setString(8, obj.getRC());
	}

	@Override
	public String generatedIdPK() throws SQLException {
		//i adjust here to select the max id_f always
		String sql = "SELECT MAX(CAST(id_f AS UNSIGNED)) FROM Fournisseur";
		try (PreparedStatement stmt = this.connection.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				int max = rs.getInt(1);
				return String.format("%02d", max + 1);
			} else {
				return "01";
			}
		}
	}


	@Override
	protected void setUpdateParameters(PreparedStatement statement, Fournisseur obj) throws SQLException {
		statement.setString(1, obj.getAdresse());
		statement.setString(2, obj.getNum_tel());
		statement.setString(3, obj.getNom_f());
		statement.setString(4, obj.getMail_f());
		statement.setString(5, obj.getNIF());
		statement.setString(6, obj.getNIS());
		statement.setString(7, obj.getRC());
	}

	@Override
	protected String buildUpdateSetClause() {
		//I adjust the names in this methode to fit the database names and also in mapresult
		return "adresse_fournisseur = ?, numerotlph_fournisseur = ?, nom_fournisseur = ?, email_fournisseur = ?, NIF = ?, NIS = ?, RC = ?";
	}

	@Override
	public Fournisseur mapResultSetToEntity(ResultSet result) throws SQLException {
		//I adjust the names in this methode to fit the database names
		Fournisseur fournisseur = new Fournisseur();
		fournisseur.setId_f(result.getString("id_f"));
		fournisseur.setAdresse(result.getString("adresse_fournisseur"));
		fournisseur.setNum_tel(result.getString("numerotlph_fournisseur"));
		fournisseur.setNom_f(result.getString("nom_fournisseur"));
		fournisseur.setMail_f(result.getString("email_fournisseur"));
		fournisseur.setNIF(result.getString("NIF"));
		fournisseur.setNIS(result.getString("NIS"));
		fournisseur.setRC(result.getString("RC"));
		return fournisseur;
	}

	@Override
	public String getSearchCondition() {
		return "nom LIKE ? OR email LIKE ? OR NIF LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String searchPattern = "%" + keyword + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
	}
}
