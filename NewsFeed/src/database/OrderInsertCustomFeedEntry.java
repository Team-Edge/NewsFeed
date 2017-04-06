package database;

import java.sql.SQLException;

import datatypes.SourceFeedEntry;

/**
 * SQL Order to add a SourceFeedEntry to a matching filter
 */
public class OrderInsertCustomFeedEntry {
	private SqlOrder wrapped;
	
	/**
	 * standard constructor
	 * @param database			DBconnection for the server to which the instruction will be sent
	 * @param sourceFeedEntryID	ID of the SourceFeedEntry that meets the filter's condition
	 * @param FilterID			ID of the filter thats condition is met
	 * @throws SQLException		if the connection to the database fails
	 * @throws IllegalArgumentException	if database is null
	 * @see SourceFeedEntry
	 * @see DBconnection
	 */
	public OrderInsertCustomFeedEntry(DBconnection database, int sourceFeedEntryID, int FilterID) throws SQLException {
		String sql = "INSERT INTO Newsfeed.CustomFeedEntry (Filter_ID, SourceFeedEntry_ID) VALUES (?, ?); ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, FilterID);
		this.wrapped.setStmtInt(2, sourceFeedEntryID);
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

