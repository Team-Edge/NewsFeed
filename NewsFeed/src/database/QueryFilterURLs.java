package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	 */
	public QueryFilterURLs(DBconnection database, int filterID) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT SourceFeed_ID FROM Newsfeed.FilterURL WHERE Filter_ID = "+filterID+";");
	}

	/**
	 * sends the query to the server for execution
	 * 
	 * @return a list of all SourceFeedIDs that are allowed by the filter
	 * @throws Exception if connection or execution fails
	 */
	public List<Integer> getSourceFeedIDs() throws Exception {
		try {
			wrapped.query();
			ArrayList<Integer> ret = new ArrayList<Integer>();
			ResultSet result= wrapped.getResult();
			while(result.next()) {
				ret.add(result.getInt(1));
			}
			wrapped.close();
			return ret;	
		} finally {
			wrapped.close();
		}
	}
	
}

