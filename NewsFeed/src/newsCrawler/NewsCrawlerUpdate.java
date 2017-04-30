package newsCrawler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import database.DBconnection;
import database.OrderRemoveSourceFeedEntry;
import datatypes.SourceFeed;
import datatypes.SourceFeedEntry;
import genericObserver.Observable.Observer;
import program.Configuration;

/**
 * class for an update cycle that searches a SourceFeed for new entries
 */
public class NewsCrawlerUpdate implements Runnable{
	private SourceFeed toUpdate;
	private DBconnection database;

	
	/**
	 * standard constructor
	 * @param toUpdate SourceFeed which has to be updated
	 * @param database DBConnection which abstracts the corresponding DB server
	 */
	public NewsCrawlerUpdate(SourceFeed toUpdate, DBconnection database) {
		this.toUpdate = toUpdate;
		this.database = database;		
	}
	

	/**
	 * performs the update of a SourceFeed
	 * fetches the SourceFeed, extracts old and new entries, removes old ones
	 * enlarges the new ones and performs filter searches on them and adds them to filter matches
	 */
	@Override
	public void run() {
		Calendar cal;
		SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
		if (Configuration.getGeneralBeVerbose()) {
			cal = Calendar.getInstance();
			System.out.println(sdf.format(cal.getTime()) + " : Looking at " + this.toUpdate.getUrl());
		}
		List<SourceFeedEntry> newEntries = new LinkedList<SourceFeedEntry>();
		List<SourceFeedEntry> oldEntries = new LinkedList<SourceFeedEntry>();
		//fetch entries
		try {
			toUpdate.fetchEntries(newEntries, oldEntries);
		} catch (Exception e) {
			cal = Calendar.getInstance();
	        System.err.println(sdf.format(cal.getTime()) + " : Unknown error during NewsCrawlerUpdate");
			System.err.println(e.getMessage());
			System.err.println();
		}	
		
		if (!newEntries.isEmpty()) {
			//enlarge and process FeedEntries in a pipe
			//while srvEnlarger waits for an external service to enlarge a FeedEntry, 
			//srvUpdater updates the already enlarged FeedEntries
			//enlarged SourceFeedEntries are passed with the Observer pattern
			UpdateService updater = new UpdateService(this.database, this.toUpdate.getID());
			ArrayList<Observer<? super SourceFeedEntry>> observers = new ArrayList<Observer<? super SourceFeedEntry>>();
			observers.add(updater);
			EnlargingService enlarger = new EnlargingService(newEntries, observers);
			Thread srvEnlarger = new Thread(enlarger);
			Thread srvUpdater = new Thread(updater);
			srvEnlarger.start();
			srvUpdater.start();
			try {
				//wait for other threads to end
				srvUpdater.join();
			} catch (InterruptedException e) {
				cal = Calendar.getInstance();
		        System.err.println(sdf.format(cal.getTime()) + " : Unexpected interrupt while processing new entries");
				System.err.println(e.getMessage());
				System.err.println();
				srvEnlarger.interrupt();
				srvUpdater.interrupt();
				return;
			}
		}
		//remove old entries from database
		//actually: mark them as free to be removed
		for(SourceFeedEntry toRemove : oldEntries) {
			try {
				new OrderRemoveSourceFeedEntry(this.database, toRemove).execute();
			} catch (Exception e) {
				cal = Calendar.getInstance();
		        System.err.println(sdf.format(cal.getTime()) + " : Old FeedEntry could not be removed");
				System.err.println(e.getMessage());
				System.err.println();
			}
		}
	}
	
	
}
