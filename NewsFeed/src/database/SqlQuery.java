package database;


import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlQuery implements Closeable{
	private DBconnection database;
	private PreparedStatement stat;
	protected ResultSet queryResult;

	
	public SqlQuery(DBconnection database,  String sql) throws SQLException
	{
		this.database = database;
		this.stat = this.database.createStatement(sql);
		this.queryResult = null;
	}
		
	public void setStmtString(int position, String value) throws SQLException {
		stat.setString(position, value);
	}
	
	public void setStmtInt(int position, int value) throws SQLException {
		stat.setInt(position, value);
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
			synchronized(database)
			{
				this.queryResult.close();
			}
		} catch(Exception e) {
			//ignore
		}
	}
	
}
