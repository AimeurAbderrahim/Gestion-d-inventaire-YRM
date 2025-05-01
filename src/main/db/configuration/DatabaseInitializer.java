/*
 * 	DatabaseInitializer
 * 
 *   A utility class responsible for initializing a database schema by loading nessisary sql configuration
 *   from an external file. This automates table creation and schema setup during application
 *   startup, ensuring the database structure matches the application's requirements.
 *   
 *   Key Responsibilities:
 *   1. Reads SQL schema definitions from a specified file
 *   2. Executes DDL (Data Definition Language) commands to create tables
 *   3. Handles errors gracefully (existing tables, syntax errors, etc.)
 *   4. Integrates with the ConfigDatabase connection pool
 *   
 *   Typical Use Case:
 *   - Application startup initialization
 *   - Test environment setup
 *  @author Souane Abdenour , Berrached Maroua
 */
// TODO: initialisation of tables independent on application
package src.main.db.configuration;

import java.io.FileReader;
import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.Statement;

import src.main.db.configuration.ConfigDatabase;
import src.main.db.errors.ConnectionFailedException;
import src.main.db.errors.ReadSQLTableFileException;
import src.main.db.errors.ExecuteStatementException;

public class DatabaseInitializer {
	private final ConfigDatabase conn;
	private final String fileName;

	/**
	 * Constructs a new DatabaseInitializer
	 * @param conn Valid ConfigDatabase connection handler
	 * @param fileName Path to the SQL file containing table definitions
	 */
	public DatabaseInitializer(ConfigDatabase conn , String fileName){
		this.conn = conn;
		this.fileName = fileName;
	}
	/**
	 * Main initialization routine - loads all tables from the SQL file
	 * @throws ReadSQLTableFileException If the SQL file cannot be read
	 * @throws ConnectionFailedException If database connection fails
	 */
	private void loadTables() throws ReadSQLTableFileException , ConnectionFailedException {
		BufferedReader br = new BufferedReader(new FileReader(this.fileName));
		StringBuilder tab = new StringBuilder();
		Statement stmt = conn.createStatement();
		String line;

		while((line = br.readLine()) != null) {
			tab.append(line);
			if(line.trim().endsWith(";")) {
				String sqlCommand = sqlBuilder.toString();
				loadTable(stmt, sqlCommand);
				sqlBuilder.setLength(0);
			}
		}

		br.close();
	}
	/**
	 * Executes a single table creation command
	 * @param statement Active SQL Statement object
	 * @param table SQL DDL command (CREATE TABLE, etc.)
	 * @throws ExecuteStatementException For non-recoverable SQL errors
	 */
	private void loadTable(Statement statement , String table) throws ExecuteStatementException {
		try {
			stmt.execute(table);
		} catch (ExecuteStatementException e) {
			if (!e.getMessage().contains("already exists")) {
				throw e;
			}
		}
	}
}
