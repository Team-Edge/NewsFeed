package database;

import java.sql.SQLException;
import datatypes.SourceFeedEntry;

/**
 * SQL Order to remove all SourceFeedEntries(and their profile entries) and add them to a separate archive table
 */
public class OrderArchiveOldSourceFeedEntries {
	private SqlOrder wrapped;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the instruction will be sent
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @throws IllegalArgumentException	if database is null
	 * @see SourceFeedEntry
	 * @see DBconnection
	 */
	public OrderArchiveOldSourceFeedEntries(DBconnection database) throws SQLException {
		String sql = "DELETE FROM SourceFeedEntry WHERE SourceFeedEntry.toRemove = 1 AND NOT EXISTS"
				+ "(SELECT * FROM CustomFeedEntry cfe WHERE cfe.SourceFeedEntry_ID = SourceFeedEntry.ID AND cfe.sent = 0);";
		this.wrapped = new SqlOrder(database, sql);
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

