package database;

import java.sql.SQLException;
import datatypes.SourceFeedEntry;

public class OrderRemoveSourceFeedEntry {
	private SqlOrder wrapped;
	
	public OrderRemoveSourceFeedEntry(DBconnection database, SourceFeedEntry entry) throws SQLException {
		String sql = "UPDATE Newsfeed.SourceFeedEntry SET toRemove = 1 WHERE URL = ?;";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtString(1, entry.getURL());
	}
	
	public int execute() throws Exception {
		return this.wrapped.execute();
	}
	
}

