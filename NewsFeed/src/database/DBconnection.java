package database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Class for wrapping and simplifying a jdbc database connection
 * 
 * @author Florian
 */
public class DBconnection implements Closeable {
	private String connectionString;
	private String username;
	private String password;	
	private Connection con;
	private boolean closed;
	
	/**
	 * standard constructor, tries establish a connection to the server automatically
	 * 
	 * @param connectionString	A string formed like "jdbc:[IPadress]:[port]/[Schema]
	 * @param username			login name for DB server
	 * @param password			login password for the specified user
	 * @throws SQLException		if the database failed connecting to the server
	 */
	public DBconnection(String connectionString, String username, String password) throws SQLException {
		this.connectionString = connectionString;
		this.username = username;
		this.password = password;
		this.closed = false;
		try {
			this.con = DriverManager.getConnection(connectionString, username, password);
		} catch (SQLException e) {
			this.con = null;
			throw e;
		}
	}
	
	/**
	 * Connection refresh, called automatically
	 * 
	 * @throws SQLException	if the refresh failed
	 */
	private void refreshConnection() throws SQLException {
		if(this.closed == true) {
			throw new SQLException("Connection has already been closed manually");
		}
		try {
			this.con.close();
		} catch (SQLException e) {}
		try {
			this.con = DriverManager.getConnection(connectionString, username, password);
		} catch (SQLException e) {
			this.con = null;
			throw e;
		}
	}
	
	/**
	 * Creates a PreparedStatement that is secure against SQL insertions if used correctly
	 * @param sql			SQL query String. Can also contain '?' Characters that can be replaced. 
	 * @return				a statement that can be executed
	 * @throws SQLException	if the statement contains errors or the S´server connection failed
	 */
	public PreparedStatement createStatement(String sql) throws SQLException {
		try {
			return this.con.prepareStatement(sql);
		}
		catch (SQLException e) {
			try {
				this.refreshConnection();
				return this.con.prepareStatement(sql);
			}
			catch (SQLException e1) {
				throw e;
			}
		}
	}
	
	/**
	 * executes a PreparedStatements on this DB as a query
	 * 
	 * @param stat			the statement that has to be executed
	 * @return				ResultSet with the query result
	 * @throws SQLException if the statement could not be executed
	 */
	public synchronized ResultSet request(PreparedStatement stat) throws SQLException {
		try {
			return stat.executeQuery();
		}
		catch (SQLException e) {
			try {
				this.refreshConnection();
				return stat.executeQuery();
			}
			catch (SQLException e1) {
				throw e;
			}
		}
	}
	
	/**
	 * executes a statement as a non-query instruction
	 * such are insertions, deletions, updates and count instructions
	 * @param stat			the PreparedStatement having the SQL instruction
	 * @return				int containing the result, 0, or a negative number
	 * @throws Exception	if execution fails
	 */
	public synchronized int execute(PreparedStatement stat) throws Exception {
		try {
			return stat.executeUpdate();
		}
		catch (SQLException e) {
			try {
				System.err.println("Error during SQL Order: " + e.getMessage());
				this.refreshConnection();
				return stat.executeUpdate();
			}
			catch (SQLException e1) {
				throw e;
			}
		}
	}

	/**
	 * Closes the DB connection
	 * @throws IOException	if connection is already closed
	 */
	@Override
	public void close() throws IOException {
		this.closed = true;
		try {
			this.con.close();
		} catch (SQLException e) {
			throw new IOException(e.getMessage());
		}	
	}	
	
}
