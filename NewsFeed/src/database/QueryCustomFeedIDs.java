package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryCustomFeedIDs {
	private SqlQuery wrapped;
	
	public QueryCustomFeedIDs(DBconnection database) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT ID FROM Newsfeed.CustomFeed;");
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

