package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dataTypes.SourceFeed;

public class QuerySources {
	private SqlQuery wrapped;
	
	public QuerySources(DBconnection database) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT ID, URL, CacheFile FROM Newsfeed.SourceFeed;");
	}

	public List<SourceFeed> getSources() throws Exception {
		wrapped.query();
		ArrayList<SourceFeed> ret = new ArrayList<SourceFeed>();
		ResultSet result= wrapped.getResult();
		while(result.next())
			ret.add(new SourceFeed(result.getInt(1), result.getString(2), result.getString(3)));
		wrapped.close();
		return ret;	
	}
	
}

