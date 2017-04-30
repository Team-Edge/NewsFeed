package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import datatypes.SourceFeed;
import datatypes.SourceFeedFactory;

/**
 * SQL query to get all URLs to a given filter ID
 */
public class QueryFilterURLs {
	private SqlQuery wrapped;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the query will be sent
	 * @param filterID		ID of the filter for that URLs are filtered
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @throws IllegalArgumentException	if database is null
	 */
	public QueryFilterURLs(DBconnection database, int filterID) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT s.ID, s.URL, s.CacheFile FROM Newsfeed.SourceFeed s "
											+"INNER JOIN Newsfeed.FilterURL u ON s.ID = u.SourceFeed_ID "
											+"WHERE u.Filter_ID = "+filterID+";");
	}

	/**
	 * sends the query to the server for execution
	 * 
	 * @return a list of all SourceFeeds that are allowed by the filter
	 * @throws Exception if connection or execution fails
	 */
	public List<SourceFeed> getSourceFeeds() throws Exception {
		ResultSet result = null;
		try {
			wrapped.query();
			ArrayList<SourceFeed> ret = new ArrayList<SourceFeed>();
			result= wrapped.getResult();
			while(result.next()) {
				ret.add(SourceFeedFactory.createSourceFeed(result.getInt(1), result.getString(2), result.getString(3)));
			}
			return ret;	
		} finally {
			if(result!=null) {
				result.close();
			}
			wrapped.close();
		}
	}
	
	/**
	 * sends the query to the server for execution
	 * 
	 * @return a list of all SourceFeedIDs that are allowed by the filter
	 * @throws Exception if connection or execution fails
	 */
	public List<Integer> getSourceFeedIDs() throws Exception {
		ResultSet result = null;
		try {
			wrapped.query();
			ArrayList<Integer> ret = new ArrayList<Integer>();
			result= wrapped.getResult();
			while(result.next()) {
				ret.add(result.getInt(1));
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

