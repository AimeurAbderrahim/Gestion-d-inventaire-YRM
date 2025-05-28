package db.java;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import testpackage.model.core.Personne;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonneDatabase extends EntityCoreDatabase<Personne> {

	public PersonneDatabase(ConfigDatabase config, String idCol, String tableName) throws ConnectionFailedException {
		super(config, idCol == null ? "id_p" : idCol, tableName == null ? "Personne" : tableName);
	}

	public PersonneDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(conn, idCol == null ? "id_p" : idCol, tableName == null ? "Personne" : tableName);
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
	protected void setAddParameters(PreparedStatement stmt, Personne p) throws SQLException {
		stmt.setString(1, generatedIdPK());
		stmt.setString(2, p.getNom());
		stmt.setString(3, p.getPrenom());
		stmt.setDate(4, Date.valueOf(p.getDate_naissance()));
		stmt.setString(5, p.getEmail());
		stmt.setString(6, p.getAdresse());
		stmt.setString(7, p.getNumero_tlph());
		stmt.setBoolean(8, p.isAvoir_compte());
		stmt.setString(9, p.getId_emplacement());
		if (p.getId_c() == null || p.getId_c().isEmpty()) {
			stmt.setNull(10, java.sql.Types.VARCHAR);
		} else {
			stmt.setString(10, p.getId_c());
		}

	}

	@Override
	protected void setUpdateParameters(PreparedStatement stmt, Personne p) throws SQLException {
		stmt.setString(1, p.getNom());
		stmt.setString(2, p.getPrenom());
		stmt.setDate(3, Date.valueOf(p.getDate_naissance()));
		stmt.setString(4, p.getEmail());
		stmt.setString(5, p.getAdresse());
		stmt.setString(6, p.getNumero_tlph());
		stmt.setBoolean(7, p.isAvoir_compte());
		stmt.setString(8, p.getId_emplacement());
		stmt.setString(9, p.getId_c());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "nom = ?, prenom = ?, date_naissance = ?, email = ?, adresse = ?, numero_tlph = ?, avoir_compte = ?, id_emplacement = ?, id_c = ?";
	}

	@Override
	public Personne mapResultSetToEntity(ResultSet rs) throws SQLException {
		Personne p = new Personne();
		p.setId_p(rs.getString("id_p"));
		p.setNom(rs.getString("nom"));
		p.setPrenom(rs.getString("prenom"));
		p.setDate_naissance(rs.getDate("date_naissance").toLocalDate());
		p.setEmail(rs.getString("email"));
		p.setAdresse(rs.getString("adresse"));
		p.setNumero_tlph(rs.getString("numero_tlph"));
		p.setAvoir_compte(rs.getBoolean("avoir_compte"));
		p.setId_emplacement(rs.getString("id_emplacement"));
		p.setId_c(rs.getString("id_c"));
		return p;
	}

	@Override
	public String generatedIdPK() throws SQLException {
		String sql = "SELECT MAX(CAST(SUBSTRING(id_p, 2) AS UNSIGNED)) FROM Personne";
		try (PreparedStatement stmt = connection.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				int max = rs.getInt(1);
				return "P" + String.format("%03d", max + 1);
			} else {
				return "P001";
			}
		}
	}

	@Override
	public String getSearchCondition() {
		return "nom LIKE ? OR prenom LIKE ? OR email LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		String pattern = "%" + keyword + "%";
		statement.setString(1, pattern);
		statement.setString(2, pattern);
		statement.setString(3, pattern);
	}


}