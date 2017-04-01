package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import datatypes.SourceFeedEntry;
import program.Configuration;

/**
 * SQL Order to insert a new SourceFeedEntry to a database
 */
public class OrderInsertSourceFeedEntry {
	private SqlOrder wrapped;
	private SqlQuery getLastID;
	
	/**
	 * standard constructor
	 * @param database		DBconnection for the server to which the instruction will be sent
	 * @param entry			the SourceFeedEntry that will be added
	 * @param sourceFeedID	the ID of the SourceFeed to which the entry belongs 
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @see SourceFeedEntry
	 * @see DBconnection
	 */
	public OrderInsertSourceFeedEntry(DBconnection database, SourceFeedEntry entry, int sourceFeedID) throws SQLException {
		String sql = "INSERT INTO Newsfeed.SourceFeedEntry "
			+ "(SourceFeed_ID, Title, Description, Img_URL, PubDate, URL, Text) "
			+ "VALUES( ?, ?, ?, ?, ?, ?, ?); ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, sourceFeedID);
		this.wrapped.setStmtString(2, entry.getTitle());
		this.wrapped.setStmtString(3, entry.getDescription());
		this.wrapped.setStmtString(4, entry.getImgURL());
		this.wrapped.setStmtString(5, new SimpleDateFormat(Configuration.getDbSqlDateFormat()).format(entry.getPubDate()));
		this.wrapped.setStmtString(6, entry.getURL());
		this.wrapped.setStmtString(7, entry.getText());
		String sql2 = "SELECT LAST_INSERT_ID(); ";
		this.getLastID = new SqlQuery(database, sql2);
	}
	
	/**
	 * sends the instruction to the server for execution
	 * @return a non-negative value on success
	 * @throws Exception if connection or execution fails
	 */
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

