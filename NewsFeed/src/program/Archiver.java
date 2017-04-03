package program;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import database.DBconnection;
import database.OrderArchiveOldSourceFeedEntries;

/**
 * job for archiving obsolete SourceFeedEntries
 */
public class Archiver implements IApplicationJob {

	/**
	 * standard constructor
	 */
	public Archiver() {
	}

	/**
	 * moves old SourceFeedEntries to archive table
	 */
	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
		DBconnection database = null;		
		try {
			database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
										+ ":" + Configuration.getDbServerPort()
										+ "/" + Configuration.getDbServerSchemaName(), 
										Configuration.getDbServerUserName(), 
										Configuration.getDbServerPassword());
			try {
				new OrderArchiveOldSourceFeedEntries(database).execute();
			} catch (Exception e) {
				cal = Calendar.getInstance();
				System.err.println(sdf.format(cal.getTime()) + " : Executing query failed");
				System.err.println(e.getMessage());
				System.err.println();
			}
		} catch (SQLException e) {
			cal = Calendar.getInstance();
			System.err.println(sdf.format(cal.getTime()) + " : DB-Login failed");
			System.err.println(e.getMessage());
			System.err.println();
		} catch (Exception e) {
			cal = Calendar.getInstance();
			System.err.println(sdf.format(cal.getTime()) + " : Unknown error while executing NewsCrawler");
			System.err.println(e.getMessage());
			System.err.println();
		} finally {
			try {
				database.close();
			} catch (IOException e) {
				// ignore
				// program has ended and DB-Server will disconnect automatically if we could not. 
			}
		}

	}

	@Override
	public boolean needsDB() {
		return true;
	}

	@Override
	public String toString() {
		return "Archiver";
	}
	
}
