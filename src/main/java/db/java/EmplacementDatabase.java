package db.java;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import testpackage.model.core.Emplacement;

import java.sql.*;
/*
 * how to use this class :
 * 1. initialisation of the class :
 * 	example :
 *		try {
 *		    // Create the database access object
 *		    EmplacementDatabase emplacementDB = new EmplacementDatabase(null , null , null);
 *
 *		    // Or with custom ID column name
 *		    // EmplacementDatabase emplacementDB = new EmplacementDatabase("custom_id_column");
 *		} catch (ConnectionFailedException e) {
 *		    System.err.println("Failed to connect to database: " + e.getMessage());
 *		} catch (LoadPropertiesException e) {
 *		    System.err.println("Failed to load database properties: " + e.getMessage());
 *		}
 *
 * 2. using CRUD operations : check Operation interface ...
 * 	2.1. add and atchoObj method
 * 	example :
 * 		Emplacement newSupplier = new Emplacement();
 * 		newSupplier.setId_emplacement("E1001");
 * 		newSupplier.setTypeSalle(TypeSalle.BUREAUX);
 * 		newSupplier.setSuperficie(23);
 * 		newSupplier.setBureau(2);
 * 		newSupplier.setService(Services.RECHERCHES);
 *
 * 		try {
 * 		    emplacementDB.add(newSupplier);
 * 		    System.out.println("Supplier added successfully!");
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Failed to add supplier: " + e.getMessage());
 * 		}
 *
 * 	2.2. findById and atchoChwiyaObjWHakId method
 * 	example :
 * 		try {
 * 		    Emplacement foundSupplier = emplacementDB.findById("F1001");
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
 * 		    Emplacement existing = emplacementDB.findById("F1001");
 *
 * 		    if (existing != null) {
 * 		        // Create updated version
 *
 *			// Assuming copy constructor
 * 		        Emplacement updated = new Emplacement(existing);
 * 		        updated.setEmail("new.email@techsolutions.dz");
 * 		        updated.setAdresse("456 Corporate Blvd");
 *
 * 		        // Perform update
 * 		        emplacementDB.update(existing, updated);
 * 		        System.out.println("Supplier updated successfully");
 * 		    }
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Update failed: " + e.getMessage());
 * 		}
 * 	2.4. remove and gla3_3liya method
 * 	example :
 *		try {
 *		    Emplacement toDelete = emplacementDB.findById("F1001");
 *		    if (toDelete != null) {
 *		        boolean deleted = emplacementDB.remove(toDelete);
 *		        System.out.println(deleted ? "Deleted successfully" : "Deletion failed");
 *		    }
 *		} catch (OperationFailedException e) {
 *		    System.err.println("Deletion failed: " + e.getMessage());
 *		}
 *
 * 	2.5. findAll method
 * 	example :
 *		try {
 *		    List<Emplacement> allSuppliers = emplacementDB.findAll();
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
 * 		    long count = emplacementDB.countAll();
 * 		    System.out.println("There are " + count + " suppliers in the database");
 * 		} catch (OperationFailedException e) {
 * 		    System.err.println("Count operation failed: " + e.getMessage());
 * 		}
 *
 * 	2.7.
 *
 * */
public class EmplacementDatabase extends EntityCoreDatabase<Emplacement> {

	public EmplacementDatabase(ConfigDatabase config, String idCol, String tableName) throws ConnectionFailedException {
		super(config, idCol == null ? "id_emplacement" : idCol, tableName == null ? "Emplacement" : tableName);
	}

	public EmplacementDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(conn, idCol == null ? "id_emplacement" : idCol, tableName == null ? "Emplacement" : tableName);
	}

	@Override
	protected String getIdValue(Emplacement obj) {
		return obj.getId_emplacement();
	}

	@Override
	protected int getColumnCount() {
		return 5;
	}

	@Override
	protected int getUpdateParameterCount() {
		return 4;
	}

	@Override
	protected void setAddParameters(PreparedStatement stmt, Emplacement e) throws SQLException {
		stmt.setString(1, e.getId_emplacement());
		stmt.setString(2, e.getType_salle());
		stmt.setDouble(3, e.getSuperficie());
		stmt.setInt(4, e.getBureau());
		stmt.setString(5, e.getNom_service());
	}

	@Override
	protected void setUpdateParameters(PreparedStatement stmt, Emplacement e) throws SQLException {
		stmt.setString(1, e.getType_salle());
		stmt.setDouble(2, e.getSuperficie());
		stmt.setInt(3, e.getBureau());
		stmt.setString(4, e.getNom_service());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "type_salle = ?, superficie = ?, bureau = ?, nom_service = ?";
	}

	@Override
	public Emplacement mapResultSetToEntity(ResultSet rs) throws SQLException {
		Emplacement e = new Emplacement();
		e.setId_emplacement(rs.getString("id_emplacement"));
		e.setType_salle(rs.getString("type_salle"));
		e.setSuperficie(rs.getDouble("superficie"));
		e.setBureau(rs.getInt("bureau"));
		e.setNom_service(rs.getString("nom_service"));
		return e;
	}

	@Override
	public String generatedIdPK() throws SQLException {
		return null ;
	}

	@Override
	public String getSearchCondition() {
		return "type_salle LIKE ? OR nom_service LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement stmt, String keyword) throws SQLException {
		String pattern = "%" + keyword + "%";
		stmt.setString(1, pattern);
		stmt.setString(2, pattern);
	}
}
