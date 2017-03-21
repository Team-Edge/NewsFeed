package database;


import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SqlOrder {
	private PreparedStatement stat;
	private DBconnection database;

	
	public SqlOrder(DBconnection database,  String sql) throws SQLException
	{
		this.database = database;
		this.stat = this.database.createStatement(sql);
	}
	
	public int execute() throws Exception
	{
		synchronized(database)
		{
			return database.execute(this.stat);
		}
	}
	
}
