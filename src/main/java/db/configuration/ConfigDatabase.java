/**
 * 	ConfigDatabase - Centralized database connection manager
 * 
 *  Handles all low-level JDBC connection operations including:
 *  - Connection creation and validation
 *  - Credential management
 *  - Connection lifecycle control
 *  
 *  Why this class exists:
 *  	1. Encapsulates sensitive database credentials
 *  	2. Provides consistent connection handling across the application
 *  	3. Implements standardized error handling
 *  	4. Serves as single point of truth for DB configuration
 *
 *  Usage Example:
 *  	ConfigDatabase dbConfig = new ConfigDatabase();
 *  	try (Connection conn = dbConfig.getConnection()) {
 *  	    // Execute database operations
 *  	}
 *
 *   @author Souane Abdenour , Berrached Maroua
 */

package db.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import db.errors.ConnectionFailedException;
import db.errors.CloseConnectionException;

public class ConfigDatabase {
	private final String username;
	private final String password;
	private final String url;
	private Connection connection;

	public ConfigDatabase(){
		this.url 	= "jdbc:mysql://localhost:3306/project";
		//  TODO: use separate DB users with minimal required privileges
		//  TODO: Replace with secure credential loading mechanism
		//  Current implementation is for DEVELOPMENT ONLY
		this.password 	= "marvi";
		this.username 	= "marvi";
	}
	/**
	 * Establishes a new database connection
	 * @return Active Connection object
	 * @throws ConnectionFailedException If connection cannot be established
	 */
	public Connection getConnection() throws ConnectionFailedException {
		try{
			if(this.connection == null || this.connection.isClosed())
			{
				this.connection = DriverManager.getConnection(this.url, this.username, this.password);
			}
			return this.connection;
		}catch(SQLException error) {
			throw new ConnectionFailedException("Connection failed : " + error.getMessage() , error);
		}
	}
	/**
	 * Tests if database is reachable with current credentials
	 * @return true if connection succeeds, false otherwise
	 */
	public boolean testConnection() {
		try (Connection conn = this.getConnection()) {
			return !conn.isClosed();
		} catch (Exception e) {
			return false;
		}
	}
	/**
	 * Safely closes the active database connection
	 * @throws CloseConnectionException If closing fails
	 */
	public void closeConnection() throws CloseConnectionException  {
		if (this.connection != null) {
			try{
				this.connection.close();
			}catch(SQLException e){
				throw new CloseConnectionException("Failed to close connection " , e);
			}
		}
	}
}
