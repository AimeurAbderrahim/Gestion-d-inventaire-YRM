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
 *
 *   WARNING : this class is not executed by default , 
 *   	this class is used if it was a problem with tables or deleted table by mistake , 
 *   	this class will initialized all tables this database require a hight prevligies user to execute DROP CREATE databases and tables,
 *   	this class should not be visible to others
 *  @author Souane Abdenour , Berrached Maroua
 */


// TODO: this shit is not important
package db.configuration;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import db.configuration.ConfigDatabase;
import db.errors.ConnectionFailedException;
import db.errors.ReadSQLTableFileException;
import db.errors.ExecuteStatementException;
import db.errors.OperationFailedException;

public class DatabaseInitializer {
	private final ConfigDatabase conn;
	private final Connection con;
	private static final String fileName = "/db/gestiondinventaireyrm/tables.sql";

	/**
	 * Constructs a new DatabaseInitializer
	 * @param conn Valid ConfigDatabase connection handler
	 * @param fileName Path to the SQL file containing table definitions
	 */
	public DatabaseInitializer(ConfigDatabase conn) throws ExecuteStatementException , ConnectionFailedException , OperationFailedException{
		this.conn = conn;
		this.con = this.conn.getConnection();
		try{
			Statement statement = this.con.createStatement();
			if(!statement.execute("DROP DATABASE IF EXISTS project")){
				throw new ExecuteStatementException("cannot drop database project");
			}
		}catch(SQLException error){
			throw new OperationFailedException("fuck off" , error);
		}
	}
	/**
	 * Main initialization routine - loads all tables from the SQL file
	 * @throws ReadSQLTableFileException If the SQL file cannot be read
	 * @throws ConnectionFailedException If database connection fails
	 */
	public void loadTables() throws ReadSQLTableFileException , ConnectionFailedException {
		BufferedReader br = null;
		try(Connection connection = conn.getConnection()){
			InputStream input = getClass().getResourceAsStream(this.fileName);
			InputStreamReader reader = new InputStreamReader(input);
			br = new BufferedReader(reader);
			StringBuilder tab = new StringBuilder();
			Statement stmt = connection.createStatement();
			String line;

			while((line = br.readLine()) != null) {
				tab.append(line);
				if(line.trim().endsWith(";")) {
					loadTable(stmt, tab.toString());
					tab.setLength(0);
				}
			}
		}catch(SQLException e)
		{
			throw new ConnectionFailedException("database operation failed" , e);
		}catch(Exception error)
		{
			throw new ReadSQLTableFileException("read sql file Failed" , error);
		}	
	}
	/**
	 * Executes a single table creation command
	 * @param statement Active SQL Statement object
	 * @param table SQL DDL command (CREATE TABLE, etc.)
	 * @throws ExecuteStatementException For non-recoverable SQL errors
	 */
	private void loadTable(Statement statement , String table) throws ExecuteStatementException {
		try {
			statement.execute(table);
		} catch (SQLException e) {
			if (!e.getMessage().contains("already exists")) {
				throw new ExecuteStatementException("Execute SQL" , e);
			}
		}
	}
}
