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
 *   @author Souane Abdenour (rayden) , Berrached Maroua (marvi)
 */

package db.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;

import db.errors.ConnectionFailedException;
import db.errors.CloseConnectionException;
import db.errors.LoadPropertiesException;

public class ConfigDatabase {
	private final String username;
	private final String password;
	private final String url;
	private Connection connection;
	private Properties proptiesFile;

	public ConfigDatabase() throws LoadPropertiesException{
		this.proptiesFile = new Properties();
		// TODO: use separate DB users with minimal required privileges
		try(InputStream inputProp = getClass().getClassLoader().getResourceAsStream("db.properties")){
			this.proptiesFile.load(inputProp);
			this.url 	= this.proptiesFile.getProperty("db.url");
			this.username 	= this.proptiesFile.getProperty("db.user");
			this.password 	= this.proptiesFile.getProperty("db.password");
		}catch(IOException error) {
			throw new LoadPropertiesException("cannot load property file");
		}
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
