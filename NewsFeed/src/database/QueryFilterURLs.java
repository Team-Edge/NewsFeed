package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryFilterURLs {
	private SqlQuery wrapped;
	
	public QueryFilterURLs(DBconnection database, int filterID) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT SourceFeed_ID FROM Newsfeed.FilterURL WHERE Filter_ID = "+filterID+";");
	}

	public List<Integer> getSourceFeedIDs() throws Exception {
		wrapped.query();
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ResultSet result= wrapped.getResult();
		while(result.next())
			ret.add(result.getInt(1));
		wrapped.close();
		return ret;	
	}
	
}

