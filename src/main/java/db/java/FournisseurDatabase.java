package db.java;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.Fournisseur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FournisseurDatabase extends EntityCoreDatabase<Fournisseur> {

	public FournisseurDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super((idCol == null) ? "id_f" : idCol, (tableName == null) ? "Fournisseur" : tableName);
	}

	public FournisseurDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(conn, (idCol == null) ? "id_f" : idCol, (tableName == null) ? "Fournisseur" : tableName);
	}

	public FournisseurDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(conn, (idCol == null) ? "id_f" : idCol, (tableName == null) ? "Fournisseur" : tableName);
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
		statement.setString(1, this.generatedIdPK());
		statement.setString(2, obj.getAdresse());
		statement.setString(3, obj.getNumero_tlph());
		statement.setString(4, obj.getNom());
		statement.setString(5, obj.getEmail());
		statement.setString(6, obj.getNIF());
		statement.setString(7, obj.getNIS());
		statement.setString(8, obj.getRC());
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Fournisseur obj) throws SQLException {
		statement.setString(1, obj.getAdresse());
		statement.setString(2, obj.getNumero_tlph());
		statement.setString(3, obj.getNom());
		statement.setString(4, obj.getEmail());
		statement.setString(5, obj.getNIF());
		statement.setString(6, obj.getNIS());
		statement.setString(7, obj.getRC());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "adresse = ?, numero_tlph = ?, nom = ?, email = ?, NIF = ?, NIS = ?, RC = ?";
	}

	@Override
	public Fournisseur mapResultSetToEntity(ResultSet result) throws SQLException {
		Fournisseur fournisseur = new Fournisseur();
		fournisseur.setId_f(result.getString("id_f"));
		fournisseur.setAdresse(result.getString("adresse"));
		fournisseur.setNumero_tlph(result.getString("numero_tlph"));
		fournisseur.setNom(result.getString("nom"));
		fournisseur.setEmail(result.getString("email"));
		fournisseur.setNIF(result.getString("NIF"));
		fournisseur.setNIS(result.getString("NIS"));
		fournisseur.setRC(result.getString("RC"));
		return fournisseur;
	}

	@Override
	public String generatedIdPK() throws SQLException {
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
	public String getSearchCondition() {
		return "nom LIKE ? OR email LIKE ? OR NIF LIKE ? OR RC = ? ";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String pattern = "%" + keyword + "%";
		statement.setString(1, pattern);
		statement.setString(2, pattern);
		statement.setString(3, pattern);
		statement.setString(4, pattern);
	}
}
