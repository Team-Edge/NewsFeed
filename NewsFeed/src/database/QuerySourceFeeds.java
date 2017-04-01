package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datatypes.SourceFeed;

/**
 * SQL query to get all SourceFeed IDs
 */
public class QuerySourceFeeds {
	private SqlQuery wrapped;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the query will be sent
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @see DBconnection
	 */
	public QuerySourceFeeds(DBconnection database) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT ID, URL, CacheFile FROM Newsfeed.SourceFeed;");
	}

	/**
	 * sends the query to the server for execution
	 * 
	 * @return a list of all SourceFeedIDs
	 * @throws Exception if connection or execution fails
	 */
	public List<SourceFeed> getSources() throws Exception {
		try {
			wrapped.query();
			ArrayList<SourceFeed> ret = new ArrayList<SourceFeed>();
			ResultSet result= wrapped.getResult();
			while(result.next()) {
				ret.add(new SourceFeed(result.getInt(1), result.getString(2), result.getString(3)));
			}
			return ret;	
		} finally {
			wrapped.close();
		}
	}
	
}

