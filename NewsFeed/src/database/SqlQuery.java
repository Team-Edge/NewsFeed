package database;


import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlQuery implements Closeable{
	private PreparedStatement stat;
	private DBconnection database;
	protected ResultSet queryResult;

	
	public SqlQuery(DBconnection database,  String sql) throws SQLException
	{
		this.database = database;
		this.stat = this.database.createStatement(sql);
		this.queryResult = null;
	}
		
	public void query() throws Exception
	{
		synchronized(database)
		{
			this.queryResult = database.request(this.stat);
		}
	}
	
	public ResultSet getResult()
	{
		return this.queryResult;
	}

	@Override
	public void close() throws IOException {
		try {
			this.queryResult.close();
		} catch(Exception e) {
			//ignore
		}
	}
	
}
