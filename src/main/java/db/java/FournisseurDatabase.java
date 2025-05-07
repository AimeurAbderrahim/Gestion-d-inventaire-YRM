/**
 * FournisseurDatabase - Data Access Layer implementation for the Fournisseur (Supplier) entity.
 * 
 * This class provides CRUD operations and database interactions specifically for the Fournisseur table,
 * extending the generic EntityCoreDatabase class with Fournisseur-specific implementations.
 */

package db.java;

import java.sql.SQLException;

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
 *		    FournisseurDatabase fournisseurDB = new FournisseurDatabase();
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
 * 	2.1. add method
 * 	example : 
 * 		Fournisseur newSupplier = new Fournisseur();
 * 		newSupplier.setIdF("F1001");
 * 		newSupplier.setNom("Tech Solutions Inc.");
 * 		newSupplier.setAdresse("123 Business Ave");
 * 		newSupplier.setNumeroTlph("0550123456");
 * 		newSupplier.setEmail("contact@techsolutions.dz");
 * 		newSupplier.setNIF("NIF123456789");
 * 		newSupplier.setNIS("NIS987654321");
 * 		newSupplier.setIA("IA456123789");
 * 		newSupplier.setRC("RC789456123");
 * 		
 * 		try {
 * 		    fournisseurDB.add(newSupplier);
 * 		    System.out.println("Supplier added successfully!");
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Failed to add supplier: " + e.getMessage());
 * 		}
 *
 * 	2.2. findById method
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
 * 	2.3. update method
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
 * 	2.4. delete method
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

	/**
	 * Constructor with customizable ID column name.
	 * Initializes the database connection and sets the table name and ID column.
	 * 
	 * @param idCol The name of the ID column in the Fournisseur table
	 * @throws ConnectionFailedException if database connection cannot be established
	 */
	public FournisseurDatabase(String idCol) throws ConnectionFailedException {
		super.idColumn = idCol;
		super.tableName = "Fournisseur";
	}

	/**
	 * Default constructor using standard column names.
	 * Initializes with default ID column name "id_f" and table name "Fournisseur".
	 * 
	 * @throws ConnectionFailedException if database connection cannot be established
	 */
	public FournisseurDatabase() throws ConnectionFailedException, LoadPropertiesException {
		super("id_f", "Fournisseur");
	}

	/**
	 * Retrieves the ID value from a Fournisseur object.
	 * 
	 * @param obj The Fournisseur object to get the ID from
	 * @return The ID value as a String
	 */
	@Override
	protected String getIdValue(Fournisseur obj) {
		return obj.getId_f();
	}

	/**
	 * Returns the number of columns in the Fournisseur table.
	 * Used for building SQL insert statements.
	 * @return The total number of columns (9)
	 */
	@Override
	protected int getColumnCount() {
		return 9; 
	}

	/**
	 * Returns the number of parameters needed for update operations.
	 * Excludes the ID column which is used in the WHERE clause.
	 * @return The number of update parameters (8)
	 */
	@Override
	protected int getUpdateParameterCount() {
		return 8;
	}

	/**
	 * Sets parameters for an INSERT statement using values from a Fournisseur object.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param obj The Fournisseur object containing data to insert
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	protected void setAddParameters(PreparedStatement statement, Fournisseur obj) throws SQLException {
		statement.setString(1, obj.getIdF());
		statement.setString(2, obj.getAdresse());
		statement.setString(3, obj.getNum_tel());
		statement.setString(4, obj.getNom_f());
		statement.setString(5, obj.getMail_f());
		statement.setString(6, obj.getNIF());
		statement.setString(7, obj.getNIS());
		statement.setString(8, obj.getIA());
		statement.setString(9, obj.getRC());
	}

	/**
	 * Sets parameters for an UPDATE statement using values from a Fournisseur object.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param obj The Fournisseur object containing updated values
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	protected void setUpdateParameters(PreparedStatement statement, Fournisseur obj) throws SQLException {
		statement.setString(1, obj.getAdresse());
		statement.setString(2, obj.getNumeroTlph());
		statement.setString(3, obj.getNom());
		statement.setString(4, obj.getEmail());
		statement.setString(5, obj.getNIF());
		statement.setString(6, obj.getNIS());
		statement.setString(7, obj.getIA());
		statement.setString(8, obj.getRC());
	}

	/**
	 * Builds the SET clause for UPDATE statements.
	 * 
	 * @return A String containing the SET clause with all updatable columns
	 */
	@Override
	protected String buildUpdateSetClause() {
		return "adresse = ?, numero_tlph = ?, nom = ?, email = ?, NIF = ?, NIS = ?, IA = ?, RC = ?";
	}

	/**
	 * Maps a ResultSet row to a Fournisseur object.
	 * 
	 * @param result The ResultSet containing database row data
	 * @return A populated Fournisseur object
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public Fournisseur mapResultSetToEntity(ResultSet result) throws SQLException {
		Fournisseur fournisseur = new Fournisseur();
		fournisseur.setIdF(result.getString("id_f"));
		fournisseur.setAdresse(result.getString("adresse"));
		fournisseur.setNumeroTlph(result.getString("numero_tlph"));
		fournisseur.setNom(result.getString("nom"));
		fournisseur.setEmail(result.getString("email"));
		fournisseur.setNIF(result.getString("NIF"));
		fournisseur.setNIS(result.getString("NIS"));
		fournisseur.setIA(result.getString("IA"));
		fournisseur.setRC(result.getString("RC"));
		return fournisseur;
	}

	/**
	 * Generates the WHERE clause for search operations.
	 * Searches in nom, email, and NIF fields using LIKE pattern matching.
	 * 
	 * @param keyword The search term to look for
	 * @return A String containing the search condition
	 */
	@Override
	public String getSearchCondition() {
		return "nom LIKE ? OR email LIKE ? OR NIF LIKE ?";
	}

	/**
	 * Sets parameters for a search operation.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param keyword The search term to look for
	 * @throws SQLException if a database access error occurs
	 */
	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String searchPattern = "%" + keyword + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
	}
}
