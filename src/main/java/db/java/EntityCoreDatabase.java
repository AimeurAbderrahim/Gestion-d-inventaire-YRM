/**
 * Abstract base class implementing core database operations for entities.
 * Provides common CRUD (create , remove , update  , delete) functionality that can be extended by entity-specific implementations.
 * 
 * @param <T> The type of entity this class will operate on
 */

package db.java;

// sql local jdk package
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

// our implementation classes
import db.java.Operation;
import db.configuration.ConfigDatabase;
import db.errors.OperationFailedException;
import db.errors.ConnectionFailedException;
import db.errors.LoadPropertiesException;

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
	 * Adds a new entity to the database.
	 * 
	 * @param obj The entity object to add
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public void add(T obj) throws OperationFailedException {
		String sql = "INSERT INTO " + this.tableName + " VALUES (?" + ", ?".repeat(this.getColumnCount() - 1) + ")";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			this.setAddParameters(statement, obj);
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to add object to " + this.tableName, e);
		}
	}

	/**
	 * Updates an existing entity in the database.
	 * 
	 * @param oldObj The current version of the entity (used for ID lookup)
	 * @param newObj The new version of the entity with updated values
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public void update(T oldObj, T newObj) throws OperationFailedException {
		String sql = "UPDATE " + this.tableName + " SET " + this.buildUpdateSetClause() + " WHERE " + this.idColumn + " = ?";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			setUpdateParameters(statement, newObj);
			statement.setString(this.getUpdateParameterCount(), this.getIdValue(oldObj));
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to update object in " + this.tableName, e);
		}
	}

	/**
	 * Removes an entity from the database.
	 * 
	 * @param obj The entity to remove
	 * @return true if removal was successful, false otherwise
	 * @throws OperationFailedException If the operation fails
	 */
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

	/**
	 * Searches for an entity in the database.
	 * 
	 * @param obj The entity to search for
	 * @return true if the entity exists, false otherwise
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public boolean search(T obj) throws OperationFailedException {
		return this.existsById(this.getIdValue(obj));
	}

	/**
	 * Finds an entity by its ID.
	 * 
	 * @param id The ID of the entity to find
	 * @return The found entity, or null if not found
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public T findById(String id) throws OperationFailedException {
		String sql = "SELECT * FROM " + this.tableName + " WHERE " + this.idColumn + " = ?";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			statement.setString(1, id);
			ResultSet result = statement.executeQuery();
			if (result.next()) {
				return mapResultSetToEntity(result);
			}
			return null;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to find object by ID in " + this.tableName, e);
		}
	}

	/**
	 * Retrieves all entities from the database.
	 * Note: This may be memory-intensive for large datasets.
	 * 
	 * @return List of all entities
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public List<T> findAll() throws OperationFailedException {
		List<T> entities = new ArrayList<>();
		String sql = "SELECT * FROM " + this.tableName;

		try (Statement statement = this.connection.createStatement();
			 ResultSet result = statement.executeQuery(sql)) {
			while (result.next()) {
				entities.add(this.mapResultSetToEntity(result));
			}
			return entities;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to retrieve all objects from " + this.tableName, e);
		}
	}

	/**
	 * Counts all entities in the database.
	 * 
	 * @return The total count of entities
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public long countAll() throws OperationFailedException {
		String sql = "SELECT COUNT(*) FROM " + this.tableName;

		try (Statement statement = this.connection.createStatement(); 
				ResultSet result = statement.executeQuery(sql)) {
			return result.next() ? result.getLong(1) : 0;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to count objects in " + this.tableName, e);
		}
	}

	/**
	 * Checks if an entity exists with the given ID.
	 * 
	 * @param id The ID to check
	 * @return true if an entity exists, false otherwise
	 * @throws OperationFailedException If the operation fails
	 */
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

	/**
	 * Gets the maximum ID value from the database.
	 * 
	 * @return The highest ID value, or 0 if no records exist
	 * @throws OperationFailedException If the operation fails
	 */
	@Override
	public long getLastIndexID() throws OperationFailedException {
		String sql = "SELECT MAX(" + this.idColumn + ") FROM " + this.tableName;

		try (Statement statement = this.connection.createStatement(); 
				ResultSet result = statement.executeQuery(sql)) {
			return result.next() ? result.getLong(1) : 0;
		} catch (SQLException e) {
			throw new OperationFailedException("Failed to get last index ID from " + this.tableName, e);
		}
	}

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
	public abstract String getSearchCondition(String keyword);

	/**
	 * Sets parameters for search operations.
	 * 
	 * @param statement The PreparedStatement to set parameters on
	 * @param keyword The search term
	 * @throws SQLException If a database error occurs
	 */
	public abstract void setSearchParameters(PreparedStatement statement, String keyword) throws SQLException;

	/**
	 * Closes database resources.
	 * 
	 * @param resultSet The ResultSet to close
	 * @param statement The Statement to close
	 * @throws SQLException If a database error occurs during closing
	 */
	protected void closeResources(ResultSet resultSet, Statement statement) throws SQLException {
		if (resultSet != null) resultSet.close();
		if (statement != null) statement.close();
	}
}
