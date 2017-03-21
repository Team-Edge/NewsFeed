package database;

import java.sql.SQLException;

public class OrderInsertCustomFeedEntry {
	private SqlOrder wrapped;
	
	public OrderInsertCustomFeedEntry(DBconnection database, int sourceFeedEntryID, int customFeedID) throws SQLException {
		String sql = "INSERT INTO Newsfeed.CustomFeedEntry (CustomFeed_ID, SourceFeedEntry_ID) VALUES (?, ?); ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, customFeedID);
		this.wrapped.setStmtInt(2, sourceFeedEntryID);
	}
	
	public int execute() throws Exception {
		return this.wrapped.execute();
	}
	
}

