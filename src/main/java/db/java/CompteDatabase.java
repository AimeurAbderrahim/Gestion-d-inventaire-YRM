package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.Compte;
import testpackage.model.utils.ConvertEnum;
import testpackage.model.enumeration.Roles;

public class CompteDatabase extends EntityCoreDatabase<Compte> {

	public CompteDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_c" : idCol,
				(tableName == null) ? "Compte" : tableName
		     );
	}

	public CompteDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_c" : idCol,
				(tableName == null) ? "Compte" : tableName
		     );
	}

	public CompteDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_c" : idCol,
				(tableName == null) ? "Compte" : tableName
		     );
	}

	public Connection getConn(){
		return super.connection;
	}

	@Override
	protected String getIdValue(Compte obj) {
		return obj.getId_c();
	}

	@Override
	protected int getColumnCount() {
		return 4;
	}

	@Override
	protected int getUpdateParameterCount() {
		return 3;
	}

	@Override
	protected void setAddParameters(PreparedStatement statement, Compte obj) throws SQLException {
		long idx = super.countAll();
		String id = "";
		switch(obj.getRoles()){
			case ADMINISTRATEUR: id = id + "A";break;
			case SECRETAIRE:     id = id + "S";break;
			case MAGASINIER:     id = id + "M";break;
		}
		id = id + String.format("%03d", idx);
		statement.setString(1, id);
		statement.setString(2, obj.getNom_utilisateur());
		statement.setString(3, obj.getMot_de_passe());
		statement.setString(4, ConvertEnum.convertRolesToString(obj.getRoles()));
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Compte obj) throws SQLException {
		statement.setString(1, obj.getNom_utilisateur());
		statement.setString(2, obj.getMot_de_passe());
		statement.setString(3, obj.getRoles().toString());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "nom_utilisateur = ?, mot_de_passe = ?, role = ?";
	}

	@Override
	public Compte mapResultSetToEntity(ResultSet result) throws SQLException {
		Compte compte = new Compte();
		compte.setId_c(result.getString("id_c"));
		compte.setNom_utilisateur(result.getString("nom_utilisateur"));
		compte.setMot_de_passe(result.getString("mot_de_passe"));
		compte.setRoles(ConvertEnum.convertStringToRoles(result.getString("role")));
		return compte;
	}

	@Override
	public String getSearchCondition() {
		return "nom_utilisateur LIKE ? OR role LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String searchPattern = "%" + keyword + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
	}

	@Override
	public String generatedIdPK() throws SQLException {
		return null;
	}
}
