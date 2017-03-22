package newsfeed;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import database.DBconnection;
import database.QuerySourceFeeds;
import datatypes.SourceFeed;

public class Program {

	
	public static void main(String[] args)
	{
		DBconnection database = null;
		try {
			database = new DBconnection("jdbc:mysql://www.******.***:3306/Newsfeed", "*****", "******");
			
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			System.out.println("Start: "  + sdf.format(cal.getTime()));
			
			do {
				List<SourceFeed> sourceFeeds;
				try {
					sourceFeeds = new QuerySourceFeeds(database).getSources();
				} catch (Exception e) {
					e.printStackTrace();
					//wait some time
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e1) {
						//stop on interrupt
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
				}
				
			} while(false);
			
			cal = Calendar.getInstance();
			System.out.println("Stop: " + sdf.format(cal.getTime()));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//we can not work without a DB connection
			return;
		} finally {
			try {
				database.close();
			} catch (IOException e) {
				// ignore
				// program has ended and DB-Server will disconnect automatically if we could not. 
			}
		}		
	}

}
