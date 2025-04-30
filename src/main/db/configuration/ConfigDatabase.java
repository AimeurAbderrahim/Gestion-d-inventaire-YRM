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

package src.main.db.configuration;

import java.sql.Connection;
import java.sql.DriverManager;

import src.main.db.errors.ConnectionFailedException;
import src.main.db.errors.CloseConnectionException;

public class ConfigDatabase {
	private final String username;
	private final String password;
	private final String url;

	public ConfigDatabase(){
		this.username 	= "jdbc:mysql://localhost:3306/project";
		//  TODO: use separate DB users with minimal required privileges
		//  TODO: Replace with secure credential loading mechanism
		//  Current implementation is for DEVELOPMENT ONLY
		this.password 	= "marvi";
		this.url 	= "marvi";
	}
	/**
	 * Establishes a new database connection
	 * @return Active Connection object
	 * @throws ConnectionFailedException If connection cannot be established
	 */
	public Connection getConnection() throws ConnectionFailedException {
		return DriverManager.getConnection(this.url, this.username, this.password);
	}
	/**
	 * Tests if database is reachable with current credentials
	 * @return true if connection succeeds, false otherwise
	 */
	public boolean testConnection() {
		try (Connection conn = this.getConnection()) {
			return true;
		} catch (ConnectionFailedException e) {
			return false;
		}
	}
	/**
	 * Safely closes the active database connection
	 * @throws CloseConnectionException If closing fails
	 */
	public void closeConnection() throws CloseConnectionException  {
		if (connection != null) {
			connection.close();
		}
	}
}
