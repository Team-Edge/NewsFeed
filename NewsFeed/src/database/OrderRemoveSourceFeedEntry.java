package database;

import java.sql.SQLException;
import datatypes.SourceFeedEntry;

/**
 * SQL Order to mark a SourceFeedEntry as ready to remove
 */
public class OrderRemoveSourceFeedEntry {
	private SqlOrder wrapped;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the instruction will be sent
	 * @param entry			Entry that has to be removed
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @throws IllegalArgumentException	if database is null
	 * @see SourceFeedEntry
	 * @see DBconnection
	 */
	public OrderRemoveSourceFeedEntry(DBconnection database, SourceFeedEntry entry) throws SQLException {
		String sql = "UPDATE Newsfeed.SourceFeedEntry SET toRemove = 1 WHERE URL = ?;";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtString(1, entry.getURL());
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

