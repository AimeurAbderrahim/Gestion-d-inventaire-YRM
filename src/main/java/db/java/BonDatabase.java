package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import testpackage.model.core.Bon;

public class BonDatabase extends EntityCoreDatabase<Bon> {

	public BonDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_bon" : idCol,
				(tableName == null) ? "Bon" : tableName
		     );
	}

	public BonDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_bon" : idCol,
				(tableName == null) ? "Bon" : tableName
		     );
	}

	public BonDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_bon" : idCol,
				(tableName == null) ? "Bon" : tableName
		     );
	}

	@Override
	protected String getIdValue(Bon obj) {
		return obj.getIdBon();
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
	protected void setAddParameters(PreparedStatement statement, Bon obj) throws SQLException {
		// generated ID
		long idx = super.countAll();
		String key = null;
		if(obj.isBonReception()){
			key = "R" + obj.getReferenceId() + String.format("03%d" , idx);
		}else{
			key = "S";
		}
		statement.setString(1, key);

		statement.setTimestamp(2, java.sql.Timestamp.valueOf(obj.getDateBon()));
		statement.setBoolean(3, obj.isBonReception());
		statement.setString(4, obj.getReferenceId());
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Bon obj) throws SQLException {
		statement.setTimestamp(1, java.sql.Timestamp.valueOf(obj.getDateBon()));
		statement.setBoolean(2, obj.isBonReception());
		statement.setString(3, obj.getReferenceId());
	}

	@Override
	protected String buildUpdateSetClause() {
		return "bon_date = ?, bon_type = ?, id_emplacement = ?, id_f = ?";
	}

	@Override
	public Bon mapResultSetToEntity(ResultSet result) throws SQLException {
		Bon bon = new Bon();
		bon.setIdBon(result.getString("id_bon"));
		bon.setDateBon(result.getTimestamp("bon_date").toLocalDateTime());
		bon.setType(result.getBoolean("bon_type"));
		// Handle reference ID based on type
		if (bon.isBonReception()) {
			bon.setId_f(result.getString("id_f"));
		} else {
			bon.setId_emplacement(result.getString("id_emplacement"));
		}
		return bon;
	}

	@Override
	public String getSearchCondition() {
		return "id_bon LIKE ? OR bon_date LIKE ? OR bon_type LIKE ?";
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
		return null;
	}
}
