package newsCrawler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import database.DBconnection;
import database.OrderRemoveSourceFeedEntry;
import datatypes.SourceFeed;
import datatypes.SourceFeedEntry;
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
			EnlargingService srvEnlarger = new EnlargingService(newEntries);
			UpdateService srvUpdater = new UpdateService(this.database, this.toUpdate.getID());
			srvEnlarger.start();
			srvUpdater.start();
			for (SourceFeedEntry current : newEntries) {
				try {
					synchronized (current) {
						do {
							current.wait();
						} while (current.getText() == null);
					}
				} catch (InterruptedException e) {
					// interrupts not expected here
					return;
				}
				if (Configuration.getGeneralBeVerbose()) {
					cal = Calendar.getInstance();
					System.out.println(sdf.format(cal.getTime()) + " : Processing " + current.getURL());
				}
				synchronized (srvUpdater) {
					srvUpdater.addEntry(current);
				}
			}
			SourceFeedEntry last = newEntries.get(newEntries.size() - 1);
			synchronized (last) {
				try {
					last.wait();
				} catch (InterruptedException e) {
					cal = Calendar.getInstance();
			        System.err.println(sdf.format(cal.getTime()) + " : NewsCrawlerUpdate was interrupted. ");
					System.err.println(e.getMessage());
					System.err.println();
					return;
				} finally {
					srvUpdater.interrupt();
				}
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
