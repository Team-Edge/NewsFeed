package newsfeed;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import dataTypes.SourceFeedEntry;
import feedanalyser.CacheUpdate;
import feedanalyser.FeedReader;

public class NewsCrawlerUpdate {
	private String feedUrl;
	private String feedCacheFile;
	private List<SourceFeedEntry> newEntries;
	private List<SourceFeedEntry> oldEntries;
	
	
	public NewsCrawlerUpdate(String feedUrl, String feedCacheFile)
	{
		this.feedUrl = feedUrl;
		this.feedCacheFile = feedCacheFile;
		this.oldEntries = new LinkedList<SourceFeedEntry>();
		this.newEntries = new LinkedList<SourceFeedEntry>();
		
	}
	
	public void doWork()
	{
		try {
			//fetch RSS feed and compare to cached version
			CacheUpdate changeChecker = new CacheUpdate(this.feedUrl, this.feedCacheFile);
			if(!changeChecker.hasChanges()) 
				return;
			
			//if differences found: parse both versions
			String cacheUrl = (new File(this.feedCacheFile)).toURI().toURL().toString();
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

	public List<SourceFeedEntry> getNewEntries() {
		return newEntries;
	}

	public List<SourceFeedEntry> getOldEntries() {
		return oldEntries;
	}
	
}
