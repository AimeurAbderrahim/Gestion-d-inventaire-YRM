package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.logging.Logger;
import java.util.logging.Level;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.ProduitModel;
import testpackage.model.utils.ConvertEnum;

public class ProduitModeleDatabase extends EntityCoreDatabase<ProduitModel> {
	private static final Logger LOGGER = Logger.getLogger(ProduitModeleDatabase.class.getName());

	public ProduitModeleDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_modele" : idCol,
				(tableName == null) ? "ProduitModele" : tableName
		     );
		LOGGER.info("ProduitModeleDatabase initialized with table: " + this.tableName);
	}

	public ProduitModeleDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_modele" : idCol,
				(tableName == null) ? "ProduitModele" : tableName
		     );
		LOGGER.info("ProduitModeleDatabase initialized with existing connection");
	}

	public ProduitModeleDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_modele" : idCol,
				(tableName == null) ? "ProduitModele" : tableName
		     );
		LOGGER.info("ProduitModeleDatabase initialized with config connection");
	}

	@Override
	protected String getIdValue(ProduitModel obj) {
		return obj.getId_modele();
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
	protected void setAddParameters(PreparedStatement statement, ProduitModel obj) throws SQLException {
		try {
			statement.setString(1, obj.getId_modele());
			statement.setBoolean(2, obj.isType_produit());
			statement.setString(3, obj.getDesignation());
			String categorie = ConvertEnum.convertCategorieToString(obj.getCategorie());
			statement.setString(4, categorie);
			statement.setString(5, obj.getId_bon());
			
			LOGGER.info(String.format("Preparing to add product type: ID=%s, Designation=%s, Category=%s", 
				obj.getId_modele(), obj.getDesignation(), categorie));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting add parameters", e);
			throw e;
		}
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, ProduitModel obj) throws SQLException {
		try {
			statement.setBoolean(1, obj.isType_produit());
			statement.setString(2, obj.getDesignation());
			String categorie = ConvertEnum.convertCategorieToString(obj.getCategorie());
			statement.setString(3, categorie);
			statement.setString(4, obj.getId_bon());
			
			LOGGER.info(String.format("Preparing to update product type: ID=%s, Designation=%s, Category=%s", 
				obj.getId_modele(), obj.getDesignation(), categorie));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting update parameters", e);
			throw e;
		}
	}

	@Override
	protected String buildUpdateSetClause() {
		return "type_produit = ?, designation = ?, categorie = ?, id_bon = ?";
	}

	@Override
	public ProduitModel mapResultSetToEntity(ResultSet result) throws SQLException {
		try {
			ProduitModel modele = new ProduitModel();
			modele.setId_modele(result.getString("id_modele"));
			modele.setType_produit(result.getBoolean("type_produit"));
			modele.setDesignation(result.getString("designation"));
			String categorie = result.getString("categorie");
			modele.setCategorie(ConvertEnum.convertStringToCategorie(categorie));
			modele.setId_bon(result.getString("id_bon"));
			
			LOGGER.fine(String.format("Mapped result set to product type: ID=%s, Designation=%s, Category=%s",
				modele.getId_modele(), modele.getDesignation(), categorie));
				
			return modele;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error mapping result set to entity", e);
			throw e;
		}
	}

	@Override
	public String getSearchCondition() {
		return "designation LIKE ? OR categorie LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		try {
			String searchPattern = "%" + keyword + "%";
			statement.setString(1, searchPattern);
			statement.setString(2, searchPattern);
			LOGGER.fine("Set search parameters with keyword: " + keyword);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting search parameters", e);
			throw e;
		}
	}

	@Override
	public String generatedIdPK() throws SQLException {
		try {
			long idx = super.countAll();
			String newId = "MOD" + String.format("%03d", idx);
			LOGGER.info("Generated new product type ID: " + newId);
			return newId;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error generating primary key", e);
			throw e;
		}
	}
}
