package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL query to get all filter IDs
 */
public class QueryFilters {
	private SqlQuery wrapped;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the query will be sent
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @throws IllegalArgumentException	if database is null
	 * @see DBconnection
	 */
	public QueryFilters(DBconnection database) throws SQLException {
		this.wrapped = new SqlQuery(database, "SELECT ID FROM Filter;");
	}
	
	/**
	 * sends the query to the server for execution
	 * 
	 * @return a list of all valid filter IDs 
	 * @throws Exception if connection or execution fails
	 */
	public List<Integer> getIDs() throws Exception {
		ResultSet result = null;
		try {
			wrapped.query();
			ArrayList<Integer> ret = new ArrayList<Integer>();
			result = wrapped.getResult();
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

