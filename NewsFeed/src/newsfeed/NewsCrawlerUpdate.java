package newsfeed;

import java.io.File;
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
	
	
	public NewsCrawlerUpdate(SourceFeed toUpdate, DBconnection database)
	{
		this.toUpdate = toUpdate;
		this.database = database;
		this.oldEntries = new LinkedList<SourceFeedEntry>();
		this.newEntries = new LinkedList<SourceFeedEntry>();		
	}
	
	private void fetchEntries()
	{
		try {
			//fetch RSS feed and compare to cached version
			CacheUpdate changeChecker = new CacheUpdate(this.toUpdate.getUrl(), this.toUpdate.getCache());
			if(!changeChecker.hasChanges()) 
				return;
			
			//if differences found: parse both versions
			String cacheUrl = (new File(this.toUpdate.getCache())).toURI().toURL().toString();
			List<SourceFeedEntry> cachedFeedList = FeedReader.read(cacheUrl);
			changeChecker.applyToCache();	//update cache to current version
			List<SourceFeedEntry> currentFeedList = FeedReader.read(cacheUrl);
			
			//if old entries were deleted
			this.oldEntries.clear();
			if(changeChecker.hasDeletions()) {
				try {
					SourceFeedEntry oldestNew = currentFeedList.get(currentFeedList.size()-1);
					for(int i = cachedFeedList.size()-1; cachedFeedList.get(i).getURL() != oldestNew.getURL(); i--) {
						this.oldEntries.add(cachedFeedList.get(i));
					}
				} catch (IndexOutOfBoundsException e) {
					this.oldEntries.clear();
					this.oldEntries.addAll(cachedFeedList);
				}
			}
			
			//if new entries were added
			this.newEntries.clear();
			if(changeChecker.hasInsertions()) {
				try {
					SourceFeedEntry newestOld = cachedFeedList.get(0); 
					for(int i = 0; currentFeedList.get(i).getURL() != newestOld.getURL(); i++) {
						this.newEntries.add(currentFeedList.get(i));
					}
				} catch (IndexOutOfBoundsException e) {
					this.newEntries.clear();
					this.newEntries.addAll(currentFeedList);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Override
	public void run() {
		this.fetchEntries();
		
		//remove old entries from database
		//actually: mark them as free to be removed
		for(SourceFeedEntry toRemove : this.oldEntries)
		{
			try {
				new OrderRemoveSourceFeedEntry(this.database, toRemove).execute();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//enlarge and process FeedEntries in a pipe
		//while srvEnlarger waits for an external service to enlarge a FeedEntry, 
		//srvUpdater updates the already enlarged FeedEntries
		EnlargingService srvEnlarger = new EnlargingService(this.newEntries);
		srvEnlarger.start();
		UpdateService srvUpdater = new UpdateService(this.database, this.toUpdate.getId());
		for(SourceFeedEntry current : this.newEntries) {
			try {
				do {
					current.wait();
				} while(current.getText()==null);
			} catch (InterruptedException e) {
				// TODO interrupts not expected
				return;
			}
			synchronized(srvUpdater) {
				srvUpdater.addEntry(current);
			}
		}
		
	}
	
}
