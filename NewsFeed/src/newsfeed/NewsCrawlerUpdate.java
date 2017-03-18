package newsfeed;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import feedanalyser.CacheUpdate;
import feedanalyser.Feed;
import feedanalyser.FeedReader;

public class NewsCrawlerUpdate {
	private String feedUrl;
	private String feedCacheFile;
	
	
	public NewsCrawlerUpdate()
	{
		
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
			List<Feed> cachedFeedList = FeedReader.read(cacheUrl);
			changeChecker.applyToCache();	//update cache to current version
			List<Feed> currentFeedList = FeedReader.read(cacheUrl);
			
			//if old entries were deleted
			if(changeChecker.hasDeletions()) {
				List<Feed> oldEntries = new LinkedList<Feed>();
				Feed oldestNew = currentFeedList.get(currentFeedList.size()-1);
				for(int i = cachedFeedList.size()-1; cachedFeedList.get(i).getURL() == oldestNew.getURL(); i--) {
					oldEntries.add(cachedFeedList.get(i));
				}
				
				//TODO: do something with old Entries. Remove from DB? Move to separate Archive Table?
			}
			
			//if new entries were added
			if(changeChecker.hasInsertions()) {
				List<Feed> newEntries = new LinkedList<Feed>();
				Feed newestOld = cachedFeedList.get(0);
				for(int i = 0; currentFeedList.get(i).getURL() == newestOld.getURL(); i++) {
					currentFeedList.get(i).enlarge();
					newEntries.add(currentFeedList.get(i));
				}
				
				//TODO: add Entries to DB
				//		filtering
				//		matching with CustomFeeds
			}

			

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
