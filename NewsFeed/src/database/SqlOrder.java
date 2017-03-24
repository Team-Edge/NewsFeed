package database;


import java.sql.PreparedStatement;
import java.sql.SQLException;


public class SqlOrder {
	private DBconnection database;
	private PreparedStatement stat;

	
	public SqlOrder(DBconnection database,  String sql) throws SQLException {
		this.database = database;
		this.stat = this.database.createStatement(sql);
	}

	public void setStmtString(int position, String value) throws SQLException {
		stat.setString(position, value);
	}
	
	public void setStmtInt(int position, int value) throws SQLException {
		stat.setInt(position, value);
	}

	public int execute() throws Exception {
		synchronized(database) {
			return database.execute(this.stat);
		}
	}
	
}
