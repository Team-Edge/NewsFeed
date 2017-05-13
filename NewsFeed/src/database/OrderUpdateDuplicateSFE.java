package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import datatypes.SourceFeedEntry;
import program.Configuration;

public class OrderUpdateDuplicateSFE {
	private SqlOrder wrapped;
	private SqlQuery getLastID;
	
	/**
	 * standard constructor
	 * @param database		DBconnection for the server to which the instruction will be sent
	 * @param entry			the SourceFeedEntry that will be added
	 * @param sourceFeedID	the ID of the SourceFeed to which the entry belongs 
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @throws IllegalArgumentException	if database is null
	 * @see SourceFeedEntry
	 * @see DBconnection
	 */
	public OrderUpdateDuplicateSFE(DBconnection database, SourceFeedEntry entry, int sourceFeedID) throws SQLException {
		String sql = "UPDATE Newsfeed.SourceFeedEntry SET "
			+ "SourceFeed_ID=?, Title=?, Description=?, Img_URL=?, PubDate=?, Text=? "
			+ "WHERE URL=?; ";
		this.wrapped = new SqlOrder(database, sql);
		this.wrapped.setStmtInt(1, sourceFeedID);
		this.wrapped.setStmtString(2, entry.getTitle());
		this.wrapped.setStmtString(3, entry.getDescription());
		this.wrapped.setStmtString(4, entry.getImgURL());
		this.wrapped.setStmtString(5, new SimpleDateFormat(Configuration.getDbSqlDateFormat()).format(entry.getPubDate()));
		this.wrapped.setStmtString(6, entry.getText());
		this.wrapped.setStmtString(7, entry.getURL());
		String sql2 = "SELECT ID FROM Newsfeed.SourceFeedEntry WHERE URL=? ";
		this.getLastID = new SqlQuery(database, sql2);
		this.wrapped.setStmtString(1, entry.getURL());
	}
	
	/**
	 * sends the instruction to the server for execution
	 * @return a non-negative value on success
	 * @throws Exception if connection or execution fails
	 */
	public int execute() throws Exception {
		ResultSet result=null;
		this.wrapped.execute();
		try {
			this.getLastID.query();
			result = this.getLastID.getResult();
			result.next();
			int ret = result.getInt(1);
			return ret;
		} finally {
			if(result!=null) {
				result.close();
			}
			this.getLastID.close();
		}
	}

}
