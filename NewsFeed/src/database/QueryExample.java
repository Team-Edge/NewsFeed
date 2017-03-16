/**
 * 
 */
package database;


import java.sql.SQLException;

/**
 * @author Florian
 *
 */
public class QueryExample extends SqlQuery {


	public QueryExample(DBconnection database) throws SQLException {
		super(database, "SELECT * FROM SourceFeed;");
	}
	
	//The resultset has different columns for different queries, so this function depends on the query it works with. 
	//This example function regards all outputs as Strings and prints them to stdout
	public void doSomethingWithResults() throws SQLException
	{
		String row = "";
		for(int i=1; i <= queryResult.getMetaData().getColumnCount(); i++)
		{
			row += queryResult.getMetaData().getColumnLabel(i) + "; ";
		}
		System.out.println(row);
		while(queryResult.next()) {
			row = "";
			for(int i=1; i <= queryResult.getMetaData().getColumnCount(); i++)
			{
				row += queryResult.getString(i) + "; ";
			}
			System.out.println(row);
		}
	}

}
