package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryFilters {
	private SqlQuery wrapped;
	
	public QueryFilters(DBconnection database, int customFeedId) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT ID FROM Newsfeed.Filter WHERE CustomFeed_ID = "+customFeedId+";");
	}

	public List<Integer> getIDs() throws Exception {
		wrapped.query();
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ResultSet result= wrapped.getResult();
		while(result.next())
			ret.add(result.getInt(1));
		wrapped.close();
		return ret;	
	}
	
}

