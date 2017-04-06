package newsCrawler;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import database.DBconnection;
import database.QuerySourceFeeds;
import datatypes.SourceFeed;
import program.Configuration;
import program.IApplicationJob;

/**
 * job for finding new entries to extern feeds and adding them to the database
 */
public class NewsCrawler implements IApplicationJob {

	/**
	 * standard constructor
	 */
	public NewsCrawler() {
	}

	/**
	 * finds new entries to extern feeds and adding them to the database
	 */
	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
		DBconnection database = null;
		
		if(Configuration.getGeneralBeVerbose()) {
			cal = Calendar.getInstance();
			System.out.println(sdf.format(cal.getTime()) + " : NewsCrawler started");
		}
		
		try {
			database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
										+ ":" + Configuration.getDbServerPort()
										+ "/" + Configuration.getDbServerSchemaName(), 
										Configuration.getDbServerUserName(), 
										Configuration.getDbServerPassword());

			
			do {
				List<SourceFeed> sourceFeeds;
				try {
					sourceFeeds = new QuerySourceFeeds(database).getSources();
				} catch (Exception e) {
					cal = Calendar.getInstance();
					System.err.println(sdf.format(cal.getTime()) + " : Could not get SourceFeeds from DB");
					System.err.println(e.getMessage());
					System.err.println();
					try {
						Thread.sleep(Configuration.getNetworkConnectionTimeout());
					} catch (InterruptedException e1) {
						return;
					}
					//try again
					continue;
				}
				
				for(SourceFeed currentFeed : sourceFeeds) {
					NewsCrawlerUpdate update = new NewsCrawlerUpdate(currentFeed, database);
					//update is done in the main thread
					//NewsCrawlerUpdate implements Runnable, 
					//but doing all updates at the same time is not recommended
					update.run();
					
					try {
						Thread.sleep(Configuration.getServiceCyclicDelay());
					} catch (InterruptedException e1) {
						return;
					}
				}				
			} while(Configuration.getServiceInfiniteLoop());
			
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
			if(Configuration.getGeneralBeVerbose()) {
				cal = Calendar.getInstance();
				System.out.println(sdf.format(cal.getTime()) + " : NewsCrawler has ended");
			}
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
		return "NewsCrawler";
	}
	
}
