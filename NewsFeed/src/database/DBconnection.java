/**
 * 
 */
package database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Florian
 *
 */
public class DBconnection implements Closeable {

	private String connectionString;
	private String username;
	private String password;	
	private Connection con;
	
	public DBconnection(String connectionString, String username, String password) throws SQLException
	{
		this.connectionString = connectionString;	// "jdbc:mysql://127.0.0.1:3306/Newsfeed"
		this.username = username;					// "Edge"
		this.password = password;					// "*******"
		try {
			this.con = DriverManager.getConnection(connectionString, username, password);
		} catch (SQLException e) {
			this.con = null;
			throw e;
		}
	}
	
	private void refreshConnection() throws SQLException
	{
		try {
			con.close();
		} catch (SQLException e) {}
		try {
			this.con = DriverManager.getConnection(connectionString, username, password);
		} catch (SQLException e) {
			this.con = null;
			throw e;
		}
	}
	
	public PreparedStatement createStatement(String sql) throws SQLException
	{
		try {
			return this.con.prepareStatement(sql);
		}
		catch (SQLException e) {
			try {
				this.refreshConnection();
				return this.con.prepareStatement(sql);
			}
			catch (SQLException e1)
			{
				throw e1;
			}
		}
	}
	
	public synchronized ResultSet request(PreparedStatement stat) throws SQLException
	{
		try {
			return stat.executeQuery();
		}
		catch (SQLException e) {
			try {
				this.refreshConnection();
				return stat.executeQuery();
			}
			catch (SQLException e1)
			{
				throw e1;
			}
		}
	}
	
	public synchronized int execute(PreparedStatement stat) throws Exception
	{
		try {
			return stat.executeUpdate();
		}
		catch (SQLException e) {
			try {
				System.err.println("Error during SQL Order: " + e.getMessage());
				this.refreshConnection();
				return stat.executeUpdate();
			}
			catch (SQLException e1)
			{
				throw e1;
			}
		}
	}

	@Override
	public void close() throws IOException {
		try {
			this.con.close();
		} catch (SQLException e) {
			throw new IOException(e.getMessage());
		}	
	}	
	
}
