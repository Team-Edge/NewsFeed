package newsfeed;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import database.DBconnection;
import database.OrderRemoveSourceFeedEntry;
import datatypes.SourceFeed;
import datatypes.SourceFeedEntry;
import feedanalyser.CacheUpdate;
import feedanalyser.FeedReader;

public class NewsCrawlerUpdate implements Runnable{
	private SourceFeed toUpdate;
	private DBconnection database;
	private List<SourceFeedEntry> newEntries;
	private List<SourceFeedEntry> oldEntries;
	
	
	public NewsCrawlerUpdate(SourceFeed toUpdate, DBconnection database) {
		this.toUpdate = toUpdate;
		this.database = database;
		this.oldEntries = new LinkedList<SourceFeedEntry>();
		this.newEntries = new LinkedList<SourceFeedEntry>();		
	}
	
	private void fetchEntries() {
		try {
			//fetch RSS feed and compare to cached version
			CacheUpdate changeChecker = new CacheUpdate(this.toUpdate.getUrl(), this.toUpdate.getCache());
			if(!changeChecker.hasChanges()) {
				return;
			}
			
			//if differences found: parse both versions
			String cacheUrl = (new File(this.toUpdate.getCache())).toURI().toURL().toString();
			List<SourceFeedEntry> cachedFeedList = FeedReader.read(cacheUrl);
			//update cache to current version
			changeChecker.applyToCache();
			List<SourceFeedEntry> currentFeedList = FeedReader.read(cacheUrl);
			
			
			this.oldEntries.clear();
			this.newEntries.clear();
			while(!cachedFeedList.isEmpty()) {
				boolean matched = false;
				for(int i = 0; i < currentFeedList.size(); i++) {
					if(cachedFeedList.get(0).getURL().equalsIgnoreCase(currentFeedList.get(i).getURL())) {
						matched = true;
						currentFeedList.remove(i);
						break;
					}		
				}
				if(!matched) {
					this.oldEntries.add(cachedFeedList.get(0));
				}
				cachedFeedList.remove(0);
			}
			this.newEntries.addAll(currentFeedList);
			
		} catch (Exception e) {
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
	        System.err.println(sdf.format(cal.getTime()) + " : Unknown error during NewsCrawlerUpdate");
			System.err.println(e.getMessage());
			System.err.println();
		}	
	}

	@Override
	public void run() {
		Calendar cal;
		SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
		if (Configuration.getGeneralBeVerbose()) {
			cal = Calendar.getInstance();
			System.out.println(sdf.format(cal.getTime()) + " : Looking at " + this.toUpdate.getUrl());
		}
		this.fetchEntries();
		
		//remove old entries from database
		//actually: mark them as free to be removed
		for(SourceFeedEntry toRemove : this.oldEntries) {
			try {
				new OrderRemoveSourceFeedEntry(this.database, toRemove).execute();
			} catch (Exception e) {
				cal = Calendar.getInstance();
		        System.err.println(sdf.format(cal.getTime()) + " : Old FeedEntry could not be removed");
				System.err.println(e.getMessage());
				System.err.println();
			}
		}
		
		if (!this.newEntries.isEmpty()) {
			//enlarge and process FeedEntries in a pipe
			//while srvEnlarger waits for an external service to enlarge a FeedEntry, 
			//srvUpdater updates the already enlarged FeedEntries
			EnlargingService srvEnlarger = new EnlargingService(this.newEntries);
			UpdateService srvUpdater = new UpdateService(this.database, this.toUpdate.getId());
			srvEnlarger.start();
			srvUpdater.start();
			for (SourceFeedEntry current : this.newEntries) {
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
			SourceFeedEntry last = this.newEntries.get(this.newEntries.size() - 1);
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
		
	}
	
}
