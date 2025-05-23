package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.Personne;

public class PersonneDatabase extends EntityCoreDatabase<Personne> {

	public PersonneDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_p" : idCol,
				(tableName == null) ? "Personne" : tableName
		     );
	}

	public PersonneDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_p" : idCol,
				(tableName == null) ? "Personne" : tableName
		     );
	}

	public PersonneDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_p" : idCol,
				(tableName == null) ? "Personne" : tableName
		     );
	}

	@Override
	protected String getIdValue(Personne obj) {
		return obj.getId_p();
	}

	@Override
	protected int getColumnCount() {
		return 10;
	}

	@Override
	protected int getUpdateParameterCount() {
		return 9;
	}

	@Override
	protected void setAddParameters(PreparedStatement statement, Personne obj) throws SQLException {
		statement.setString(1, this.generatedIdPK());
		statement.setString(2, obj.getNom());
		statement.setString(3, obj.getPrenom());
		statement.setDate(4, java.sql.Date.valueOf(obj.getLocalDate()));
		statement.setString(5, obj.getEmail());
		statement.setString(6, obj.getAdresse());
		statement.setString(7, obj.getNumero_tel_personne());
		statement.setBoolean(8, obj.hasAcc());
		statement.setString(9, obj.getId_emplacement());
		statement.setString(10, obj.getId_c());
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Personne obj) throws SQLException {
		statement.setString(1, obj.getNom());
		statement.setString(2, obj.getPrenom());
		statement.setDate(3, java.sql.Date.valueOf(obj.getLocalDate()));
		statement.setString(4, obj.getEmail());
		statement.setString(5, obj.getAdresse());
		statement.setString(6, obj.getNumero_tel_personne());
		statement.setBoolean(7, obj.hasAcc());
		statement.setString(8, obj.getId_emplacement());
		statement.setString(9, obj.getId_c());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "nom = ?, prenom = ?, date_naissance = ?, email = ?, adresse = ?, numero_tlph = ?, id_emplacement = ?, id_c = ?";
	}

	@Override
	public Personne mapResultSetToEntity(ResultSet result) throws SQLException {
		Personne personne = new Personne();
		personne.setId_p(result.getString("id_p"));
		personne.setNom(result.getString("nom"));
		personne.setPrenom(result.getString("prenom"));
		personne.setLocalDate(result.getDate("date_naissance").toLocalDate());
		personne.setEmail(result.getString("email"));
		personne.setAdresse(result.getString("adresse"));
		personne.setNumero_tel_personne(result.getString("numero_tlph"));
		personne.setId_emplacement(result.getString("id_emplacement"));
		personne.setId_c(result.getString("id_c"));
		return personne;
	}

	@Override
	public String getSearchCondition() {
		return "nom LIKE ? OR prenom LIKE ? OR email LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String searchPattern = "%" + keyword + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
	}

	@Override
	public String generatedIdPK() throws SQLException {
		long idx = super.countAll();
		return "PER" + String.format("%03d", idx);
	}
}
