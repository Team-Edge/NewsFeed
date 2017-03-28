package database;

import java.sql.SQLException;

public class OrderInsertCustomFeedEntry {
	private SqlOrder wrapped;
	
	public OrderInsertCustomFeedEntry(DBconnection database, int sourceFeedEntryID, int FilterID) throws SQLException {
		String sql = "INSERT INTO Newsfeed.CustomFeedEntry (Filter_ID, SourceFeedEntry_ID) VALUES (?, ?); ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, FilterID);
		this.wrapped.setStmtInt(2, sourceFeedEntryID);
	}
	
	public int execute() throws Exception {
		return this.wrapped.execute();
	}
	
}

