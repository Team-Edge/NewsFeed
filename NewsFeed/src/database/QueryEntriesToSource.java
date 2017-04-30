package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import datatypes.SourceFeed;
import datatypes.SourceFeedEntry;

/**
 * SQL query to get all URLs to a given filter ID
 */
public class QueryEntriesToSource {
	private SqlQuery wrapped;
	private SourceFeed sourcefeed;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the query will be sent
	 * @param sourcefeed	SourceFeed whose entries are queried
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @throws IllegalArgumentException	if database is null
	 */
	public QueryEntriesToSource(DBconnection database, SourceFeed sourcefeed) throws SQLException {
		String sql = "SELECT ID, Title, Description, Img_URL, PubDate, URL, Text FROM Newsfeed.SourceFeedEntry "
					+ "WHERE toRemove = 0 AND SourceFeed_ID = " + sourcefeed.getID() + ";";
		this.wrapped = new SqlQuery(database, sql);
	}

	/**
	 * sends the query to the server for execution
	 * 
	 * @return a list of all SourceFeedEntries that are child of the query SourceFeed
	 * @throws Exception if connection or execution fails
	 * @see SourceFeedEntry
	 */
	public List<SourceFeedEntry> getEntries() throws Exception {
		ResultSet result = null;
		try {
			wrapped.query();
			List<SourceFeedEntry> ret = new LinkedList<SourceFeedEntry>();
			result= wrapped.getResult();
			while(result.next()) {
				ret.add(this.sourcefeed.createEntry(result.getInt(1),	//ID
											result.getString(2),		//Title
											result.getString(3), 		//Description
											result.getDate(5), 			//PubDate
											result.getString(7), 		//Text
											result.getString(6), 		//URL
											result.getString(4) ));		//Img_URL
			}
			return ret;	
		} finally {
			if(result!=null) {
				result.close();
			}
			wrapped.close();
		}
	}
	
}

