package database;


import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * class for simplifying non-query SQL instructions on a DBconnection
 * These are insertions, deletions, updates and count instructions
 */
public class SqlOrder {
	private DBconnection database;
	private PreparedStatement stat;

	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection to the server
	 * @param sql			SQL instruction string. May contain '?' characters that can be replaced later
	 * @throws SQLException if the SQL String is malformed or the connection fails
	 * @throws IllegalArgumentException	if database is null
	 */
	public SqlOrder(DBconnection database,  String sql) throws SQLException {
		if(database == null) {
			throw new IllegalArgumentException("Expected a DBconnection instead of null");
		}
		this.database = database;
		this.stat = this.database.createStatement(sql);
	}
	
	/**
	 * replaces one of the '?' Characters from the SQL instruction with a specified string
	 * 
	 * @param position		position of the field to replace: 1 is the first occurrence of '?'
	 * @param value			String value that is inserted at the specified position
	 * @throws SQLException	if there are less than position '?' characters in the SQL string or on another error
	 */
	public void setStmtString(int position, String value) throws SQLException {
		stat.setString(position, value);
	}
	
	/**
	 * replaces one of the '?' Characters from the SQL instruction with a specified Integer
	 * 
	 * @param position		position of the field to replace: 1 is the first occurrence of '?'
	 * @param value			Int value that is inserted at the specified position
	 * @throws SQLException	if there are less than position '?' characters in the SQL string or on another error
	 */
	public void setStmtInt(int position, int value) throws SQLException {
		stat.setInt(position, value);
	}

	/**
	 * executes this instruction on the server
	 * @return				integer returned from the DB server
	 * @throws Exception	if there is an error during the execution
	 */
	public int execute() throws Exception {
		synchronized(database) {
			return database.execute(this.stat);
		}
	}
	
}
