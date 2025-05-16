package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.ProduitModel;
import testpackage.model.utils.ConvertEnum;

public class ProduitModeleDatabase extends EntityCoreDatabase<ProduitModel> {

	public ProduitModeleDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_modele" : idCol,
				(tableName == null) ? "ProduitModele" : tableName
		     );
	}

	public ProduitModeleDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_modele" : idCol,
				(tableName == null) ? "ProduitModele" : tableName
		     );
	}

	public ProduitModeleDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_modele" : idCol,
				(tableName == null) ? "ProduitModele" : tableName
		     );
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
		statement.setString(1, obj.getId_modele());
		statement.setBoolean(2, obj.isType_produit());
		statement.setString(3, obj.getDesignation());
		statement.setString(4, obj.getCategorie().toString());
		statement.setString(5, obj.getId_bon());
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, ProduitModel obj) throws SQLException {
		statement.setBoolean(1, obj.isType_produit());
		statement.setString(2, obj.getDesignation());
		statement.setString(3, obj.getCategorie().toString());
		statement.setString(4, obj.getId_bon());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "type_produit = ?, designation = ?, categorie = ?, id_bon = ?";
	}

	@Override
	public ProduitModel mapResultSetToEntity(ResultSet result) throws SQLException {
		ProduitModel modele = new ProduitModel();
		modele.setId_modele(result.getString("id_modele"));
		modele.setType_produit(result.getBoolean("type_produit"));
		modele.setDesignation(result.getString("designation"));
		modele.setCategorie(ConvertEnum.convertStringToCategorie(result.getString("categorie")));
		modele.setId_bon(result.getString("id_bon"));
		return modele;
	}

	@Override
	public String getSearchCondition() {
		return "designation LIKE ? OR categorie LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String searchPattern = "%" + keyword + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
	}

	// TODO: check the generated primary key
	@Override
	public String generatedIdPK() throws SQLException {
		long idx = super.countAll();
		return "MOD" + String.format("%03d", idx);
	}
}
