package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import datatypes.SourceFeedEntry;

public class OrderInsertSourceFeedEntry {
	private SqlOrder wrapped;
	private SqlQuery getLastID;
	
	public OrderInsertSourceFeedEntry(DBconnection database, SourceFeedEntry entry, int sourceFeedID) throws SQLException {
		String sql = "INSERT INTO Newsfeed.SourceFeedEntry "
			+ "(SourceFeed_ID, Title, Description, PubDate, URL, Text) "
			+ "VALUES( ?, ?, ?, ?, ?, ?); ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, sourceFeedID);
		this.wrapped.setStmtString(2, entry.getTitle());
		this.wrapped.setStmtString(3, entry.getDescription());
		this.wrapped.setStmtString(4, new SimpleDateFormat("yyyy-MM-dd").format(entry.getPubDate()));
		this.wrapped.setStmtString(5, entry.getURL());
		this.wrapped.setStmtString(6, entry.getText());
		String sql2 = "SELECT LAST_INSERT_ID(); ";
		this.getLastID = new SqlQuery(database, sql2);
	}
	
	public int execute() throws Exception {
		this.wrapped.execute();
		this.getLastID.query();
		ResultSet result = this.getLastID.getResult();
		result.next();
		int ret = result.getInt(1);
		result.close();
		this.getLastID.close();
		return ret;
	}
	
}

