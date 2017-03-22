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

	
/*
	public static void oldmain(String[] args)
	{
		NewsCrawlerUpdate upd = new NewsCrawlerUpdate("http://www.tagesschau.de/xml/rss2", "./cacheTagesschau.txt");
		upd.doWork();
		
		System.out.println("---");
		for(datatypes.SourceFeedEntry i : upd.getOldEntries())
			System.out.println("old " + i);
		System.out.println("---");
		for(datatypes.SourceFeedEntry i : upd.getNewEntries())
		{
			i.enlarge();
			System.out.println("new " + i);
		}
		System.out.println("---");
		
		for(datatypes.SourceFeedEntry i : upd.getNewEntries())
		{
			List<String> emptyList = new LinkedList<String>();
			List<String> keyWordsColTrump = new LinkedList<String>();
			keyWordsColTrump.add("Trump");
			List<String> keyWordsColSchulz = new LinkedList<String>();
			keyWordsColSchulz.add("Schulz");
			keyWordsColSchulz.add("SPD");
			try {
				TextSearch searcher = new TextSearch(i);
				if(searcher.query(emptyList, keyWordsColTrump, emptyList))
					System.out.println("Contains Trump: " + i);
				if(searcher.query(keyWordsColSchulz, emptyList, emptyList))
					System.out.println("Contains Schulz and SPD: " + i);
				searcher.close();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		}
		System.out.println("---");
		
	}
*/	
	
	public static void main(String[] args)
	{
		DBconnection database = null;
		try {
			database = new DBconnection("jdbc:mysql://127.0.0.1:3306/newsfeed", "root", "");
			
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
