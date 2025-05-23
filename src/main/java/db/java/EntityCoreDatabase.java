/**
 * Abstract base class implementing core database operations for entities.
 * Provides common CRUD (create , remove , update  , delete) functionality that can be extended by entity-specific implementations.
 * 
 * @param <T> The type of entity this class will operate on
 */

package db.java;

// sql local jdk package
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

// our implementation classes
import db.java.Operation;
import db.configuration.ConfigDatabase;
import db.errors.OperationFailedException;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;
import db.errors.CloseConnectionException;

public abstract class EntityCoreDatabase<T> implements Operation<T> {
	protected ConfigDatabase configuration;
	protected Connection connection;
	protected String tableName;
	protected String idColumn;

	/**
	 * Constructs a new EntityCoreDatabase instance.
	 * 
	 * @param idColumn The name of the primary key column in the database table
	 * @param tableName The name of the database table
	 * @throws LoadPropertiesException If database properties cannot be loaded
	 * @throws ConnectionFailedException If database connection cannot be established
	 */
	public EntityCoreDatabase(String idColumn, String tableName) throws ConnectionFailedException, LoadPropertiesException {
		this.idColumn = idColumn;
		this.tableName = tableName;
		this.configuration = new ConfigDatabase();
		this.connection = this.configuration.getConnection();
	}

	/**
	 * Constructs a new EntityCoreDatabase instance.
	 * 
	 * @param connection connection interface used to applied operations to the database
	 * @param idColumn The name of the primary key column in the database table
	 * @param tableName The name of the database table
	 * @throws LoadPropertiesException If database properties cannot be loaded
	 * @throws ConnectionFailedException If database connection cannot be established
	 */
	public EntityCoreDatabase(Connection connection , String idColumn, String tableName){
		this.idColumn = idColumn;
		this.tableName = tableName;
		this.configuration = null;
		this.connection = connection;
	}

	/**
	 * Constructs a new EntityCoreDatabase instance.
	 * 
	 * @param con_connection configuration of connection object used to applied operations to the database
	 * @param idColumn The name of the primary key column in the database table
	 * @param tableName The name of the database table
	 * @throws LoadPropertiesException If database properties cannot be loaded
	 * @throws ConnectionFailedException If database connection cannot be established
	 */
	public EntityCoreDatabase(ConfigDatabase con_connection , String idColumn, String tableName) throws ConnectionFailedException{
		this.idColumn = idColumn;
		this.tableName = tableName;
		this.configuration = con_connection;
		this.connection = this.configuration.getConnection();
	}


	@Override
	public void add(T obj) throws OperationFailedException {
		String sql = "INSERT INTO " + this.tableName + " VALUES (?" + ", ?".repeat(this.getColumnCount() - 1) + ")";
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			this.setAddParameters(statement, obj);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to add object to " + this.tableName + e.getMessage(), e);
		}
	}

	@Override
	public void addMany(List<T> obj) throws OperationFailedException {
		for(T ob : obj){
			this.add(ob);
		}
	}

	@Override
	public void atchoObj(T obj) throws OperationFailedException {
		this.add(obj);
	}

	@Override
	public void update(T oldObj, T newObj) throws OperationFailedException {
		String sql = "UPDATE " + this.tableName + " SET " + this.buildUpdateSetClause() + " WHERE " + this.idColumn + " = ?";
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			setUpdateParameters(statement, newObj);
			//i adjust here cuz ur calling the parameters but u forgot the id_f so if u have 6 parameteres + id_f u have 7 parameters not 6
			statement.setString(this.getUpdateParameterCount()+1, this.getIdValue(oldObj));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to update object in " + this.tableName, e);
		}
	}

	@Override
	public void bdlBdl(T oldObj, T newObj) throws OperationFailedException {
		this.update(oldObj , newObj);
	}

	@Override
	public boolean remove(T obj) throws OperationFailedException {
		String sql = "DELETE FROM " + this.tableName + " WHERE " + this.idColumn + " = ?";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			statement.setString(1, this.getIdValue(obj));
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to remove object from " + this.tableName, e);
		}
	}

	@Override
	public boolean gla3_3liya(T obj) throws OperationFailedException{
		return this.remove(obj);
	}

	@Override
	public List<T> search(String pattren) throws OperationFailedException {
		String sql = "SELECT * FROM " + this.tableName+ " WHERE " + this.getSearchCondition() ;
		List<T> res = new ArrayList<>();
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			this.setSearchParameters(statement , pattren);
			ResultSet result = statement.executeQuery();
			while(result.next()){
				T obj = mapResultSetToEntity(result);
				res.add(obj);
			}
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to remove object from " + this.tableName, e);
		}
		return res.isEmpty() ? null : res;
	}

	@Override
	public T findById(String id) throws OperationFailedException {
		String sql = "SELECT * FROM " + this.tableName + " WHERE " + this.idColumn + " = ?";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();
			return result.next() ? mapResultSetToEntity(result) : null;
			// if (result.next()) {
			// 	return mapResultSetToEntity(result);
			// }
			// return null;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to find object by ID in " + this.tableName, e);
		}
		return null; // unreachable
	}
	public T atchoChwiyaObjWHakId(String id) throws OperationFailedException{
		return this.findById(id);
	}

	@Override
	public List<T> findAll() throws OperationFailedException {
		List<T> entities = new ArrayList<>();
		String sql = "SELECT * FROM " + this.tableName;

		try (PreparedStatement statement = this.connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
			while (result.next()) {
				entities.add(this.mapResultSetToEntity(result));
			}
			return entities;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to retrieve all objects from " + this.tableName, e);
		}
	}

	public List<T> atchoGa3() throws OperationFailedException{
		return this.findAll();
	}

	@Override
	public long countAll() throws OperationFailedException {
		String sql = "SELECT COUNT(*) FROM " + this.tableName;

		try (PreparedStatement  statement = this.connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
			return result.next() ? result.getLong(1) : 0;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to count objects in " + this.tableName, e);
		}
	}

	@Override
	public boolean existsById(String id) throws OperationFailedException {
		String sql = "SELECT 1 FROM " + this.tableName + " WHERE " + this.idColumn + " = ? LIMIT 1";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();
			return result.next();
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to check existence by ID in " + this.tableName, e);
		}
	}

	// @Override
	// public long getLastIndexID() throws OperationFailedException {
	// 	String sql = "SELECT MAX(" + this.idColumn + ") FROM " + this.tableName;

	// 	try (PreparedStatement   statement = this.connection.prepareStatement(sql); ResultSet result = statement.executeQuery()) {
	// 		return result.next() ? result.getLong(1) : 0;
	// 	} catch (SQLException e) {
	// 		throw new OperationFailedException("Failed to get last index ID from " + this.tableName, e);
	// 	}
	// }

	// =============== ABSTRACT METHODS ===============

	/**
	 * Gets the ID value from an entity object.
	 * 
	 * @param obj The entity object
	 * @return The ID value
	 */
	protected abstract String getIdValue(T obj);

	/**
	 * Gets the number of columns in the database table.
	 * 
	 * @return The column count
	 */
	protected abstract int getColumnCount();

	/**
	 * Gets the number of parameters needed for update operations.
	 * 
	 * @return The parameter count
	 */
	protected abstract int getUpdateParameterCount();

	/**
	 * Sets parameters for an INSERT statement.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param obj The entity containing data to insert
	 * @throws SQLException If a database error occurs
	 */
	protected abstract void setAddParameters(PreparedStatement statement, T obj) throws SQLException;

	/**
	 * Sets parameters for an UPDATE statement.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param obj The entity containing updated data
	 * @throws SQLException If a database error occurs
	 */
	protected abstract void setUpdateParameters(PreparedStatement statement, T obj) throws SQLException;

	/**
	 * Builds the SET clause for UPDATE statements.
	 * 
	 * @return The SET clause string
	 */
	protected abstract String buildUpdateSetClause();

	/**
	 * Maps a ResultSet row to an entity object.
	 * 
	 * @param result The ResultSet containing database row data
	 * @return The populated entity object
	 * @throws SQLException If a database error occurs
	 */
	public abstract T mapResultSetToEntity(ResultSet result) throws SQLException;

	/**
	 * Generates the WHERE clause for search operations.
	 * 
	 * @param keyword The search term
	 * @return The search condition string
	 */
	public abstract String getSearchCondition();

	/**
	 * Sets parameters for search operations.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param keyword The search term
	 * @throws SQLException If a database error occurs
	 */
	public abstract void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException;



	public abstract String generatedIdPK() throws SQLException;

	/**
	 * Closes database resources.
	 * 
	 * @param resultSet The ResultSet to close
	 * @param statement The Statement to close
	 * @throws SQLException If a database error occurs during closing
	 */
	protected void closeResources(ResultSet resultSet, PreparedStatement statement) throws SQLException {
		if (resultSet != null) resultSet.close();
		if (statement != null) statement.close();
	}

	protected void clean() throws CloseConnectionException{
		if(this.configuration != null || this.configuration.testConnection())
			this.configuration.closeConnection();
		try{
			this.connection.close();
		}catch(SQLException error) {
			throw new CloseConnectionException("failed to close connection" , error);
		}
	}
}
