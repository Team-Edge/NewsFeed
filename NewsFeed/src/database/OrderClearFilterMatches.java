package database;

import java.sql.SQLException;

import datatypes.SourceFeedEntry;

/**
 * SQL Order to clear all matches for a filter
 */
public class OrderClearFilterMatches {
	private SqlOrder wrapped;
	
	/**
	 * standard constructor
	 * @param database			DBconnection for the server to which the instruction will be sent
	 * @param FilterID			ID of the filter thats condition is met
	 * @throws SQLException		if the connection to the database fails
	 * @see SourceFeedEntry
	 * @see DBconnection
	 */
	public OrderClearFilterMatches(DBconnection database, int FilterID) throws SQLException {
		String sql = "DELETE FROM Newsfeed.CustomFeedEntry WHERE Filter_ID = ?; ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, FilterID);
	}
	
	/**
	 * sends the instruction to the server for execution
	 * @return a non-negative value on success
	 * @throws Exception if connection or execution fails
	 */
	public int execute() throws Exception {
		return this.wrapped.execute();
	}
	
}

