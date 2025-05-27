package db.java;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import db.errors.OperationFailedException;
import testpackage.model.core.Bon;

public class BonDatabase extends EntityCoreDatabase<Bon> {
	private static final Logger LOGGER = Logger.getLogger(BonDatabase.class.getName());

	public BonDatabase(String idCol, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		super(
				(idCol == null) ? "id_bon" : idCol,
				(tableName == null) ? "Bon" : tableName
		     );
		LOGGER.info("BonDatabase initialized with table: " + this.tableName);
	}

	public BonDatabase(Connection conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_bon" : idCol,
				(tableName == null) ? "Bon" : tableName
		     );
		LOGGER.info("BonDatabase initialized with existing connection");
	}

	public BonDatabase(ConfigDatabase conn, String idCol, String tableName) throws ConnectionFailedException {
		super(
				conn,
				(idCol == null) ? "id_bon" : idCol,
				(tableName == null) ? "Bon" : tableName
		     );
		LOGGER.info("BonDatabase initialized with config connection");
	}

	@Override
	protected String getIdValue(Bon obj) {
		return obj.getIdBon();
	}

	@Override
	protected int getColumnCount() {
		return 6;
	}

	@Override
	protected int getUpdateParameterCount() {
		return 5;
	}

	@Override
	protected void setAddParameters(PreparedStatement statement, Bon obj) throws SQLException {
		try {
			// generated ID
			long idx = super.countAll();
			String key = null;
			if(obj.isBonReception()){
				key = "R" + obj.getReferenceId() + String.format("03%d", idx);
			} else {
				key = "S";
			}
			statement.setString(1, key);
			statement.setTimestamp(2, java.sql.Timestamp.valueOf(obj.getDateBon()));
			statement.setBoolean(3, obj.isBonReception());
			statement.setBoolean(4, obj.isValid());
			if (obj.isBonReception()) {
				statement.setString(5, null); // id_emplacement
				statement.setString(6, obj.getId_f());
			} else {
				statement.setString(5, obj.getId_emplacement());
				statement.setString(6, null); // id_f
			}

			LOGGER.info(String.format("Preparing to add bon: ID=%s, Type=%s, Valid=%b", 
				key, obj.isBonReception() ? "Reception" : "Sortie", obj.isValid()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting add parameters", e);
			throw e;
		}
	}

	@Override
	protected void setUpdateParameters(PreparedStatement statement, Bon obj) throws SQLException {
		try {
			statement.setTimestamp(1, java.sql.Timestamp.valueOf(obj.getDateBon()));
			statement.setBoolean(2, obj.isBonReception());
			statement.setBoolean(3, obj.isValid());
			statement.setString(4, obj.getReferenceId());

			LOGGER.info(String.format("Preparing to update bon: ID=%s, Type=%s, Valid=%b", 
				obj.getIdBon(), obj.isBonReception() ? "Reception" : "Sortie", obj.isValid()));
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting update parameters", e);
			throw e;
		}
	}

	@Override
	protected String buildUpdateSetClause() {
		return "bon_date = ?, bon_type = ?, is_valid = ?, id_emplacement = ?, id_f = ?";
	}

	@Override
	public Bon mapResultSetToEntity(ResultSet result) throws SQLException {
		try {
			Bon bon = new Bon();
			bon.setIdBon(result.getString("id_bon"));
			bon.setDateBon(result.getTimestamp("bon_date").toLocalDateTime());
			bon.setType(result.getBoolean("bon_type"));
			bon.setValid(result.getBoolean("is_valid"));
			// Handle reference ID based on type
			if (bon.isBonReception()) {
				bon.setId_f(result.getString("id_f"));
				bon.setId_emplacement(null);
			} else {
				bon.setId_f(null);
				bon.setId_emplacement(result.getString("id_emplacement"));
			}

			LOGGER.fine(String.format("Mapped result set to bon: ID=%s, Type=%s, Valid=%b",
				bon.getIdBon(), bon.isBonReception() ? "Reception" : "Sortie", bon.isValid()));

			return bon;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error mapping result set to entity", e);
			throw e;
		}
	}

	@Override
	public String getSearchCondition() {
		return "id_bon LIKE ? OR bon_date LIKE ? OR bon_type LIKE ?";
	}

	@Override
	public void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException {
		try {
			String searchPattern = "%" + keyword + "%";
			statement.setString(1, searchPattern);
			statement.setString(2, searchPattern);
			statement.setString(3, searchPattern);
			LOGGER.fine("Set search parameters with keyword: " + keyword);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error setting search parameters", e);
			throw e;
		}
	}

	@Override
	public String generatedIdPK() throws SQLException {
		return null;
	}

	public List<Bon> filterByValid(boolean valid) throws OperationFailedException {
		try {
			String sql = "SELECT * FROM " + super.tableName + " WHERE is_valid = ?";
			List<Bon> res = new ArrayList<>();
			try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
				statement.setBoolean(1, valid);
				ResultSet result = statement.executeQuery();
				while(result.next()){
					Bon obj = mapResultSetToEntity(result);
					res.add(obj);
				}
			}
			LOGGER.info(String.format("Found %d bons with valid=%b", res.size(), valid));
			return res.isEmpty() ? null : res;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error filtering bons by valid status", e);
			throw new OperationFailedException("Failed to filter bons by valid status", e);
		}
	}

	public List<Bon> filterByType(boolean type) throws OperationFailedException {
		try {
			String sql = "SELECT * FROM " + super.tableName + " WHERE bon_type = ?";
			List<Bon> res = new ArrayList<>();
			try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
				statement.setBoolean(1, type);
				ResultSet result = statement.executeQuery();
				while(result.next()){
					Bon obj = mapResultSetToEntity(result);
					res.add(obj);
				}
			}
			LOGGER.info(String.format("Found %d bons with type=%s", res.size(), type ? "Reception" : "Sortie"));
			return res.isEmpty() ? null : res;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error filtering bons by type", e);
			throw new OperationFailedException("Failed to filter bons by type", e);
		}
	}
}
