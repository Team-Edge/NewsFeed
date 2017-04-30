package datatypes;

import java.io.File;
import java.util.Date;
import java.util.List;

import feedUtils.CacheUpdate;
import feedUtils.FeedReader;

/**
 * Data Container for a SourceFeed
 */
public class SourceFeedRSSImpl implements SourceFeed {
	private int id;
	private String url;
	private String cache;
	
	/**
	 * standard constructor
	 * 
	 * @param id	SourceFeed_ID; primary key for the database
	 * @param url	URL of the original RSS feed
	 * @param cache	path to a cache file with a local copy of the feed content from the last run
	 */
	public SourceFeedRSSImpl(int id, String url, String cache) {
		this.id = id;
		this.url = url;
		this.cache = cache;
	}

	/**
	 * returns the SourceFeed_ID of this feed
	 * SourceFeed_ID is used as primary key for the database
	 * @return the SourceFeed_ID of this feed
	 */
	@Override
	public int getID() {
		return id;
	}

	/**
	 * returns the URL of this SourceFeed
	 * @return the URL of this SourceFeed
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * returns the path of the local cachefile
	 * @return the path of the local cachefile
	 */
	@Override
	public String getCache() {
		return cache;
	}

	
	/**
	 * fetches entries from the SourceFeed to be updated and extracts the old and new entries
	 * it also reads and updates the local cachefiles
	 * @throws Exception if connection fails or source is malformed or invalid
	 * @see feedUtils.CacheUpdate
	 */
	@Override
	public void fetchEntries(List<SourceFeedEntry> newEntries, List<SourceFeedEntry> oldEntries) throws Exception {
		//fetch RSS feed and compare to cached version
		CacheUpdate changeChecker = new CacheUpdate(this.getUrl(), this.getCache());
		if(!changeChecker.hasChanges()) {
			return;
		}
		//if differences found: parse both versions
		String cacheUrl = (new File(this.getCache())).toURI().toURL().toString();
		List<SourceFeedEntry> cachedFeedList = FeedReader.read(cacheUrl);
		//update cache to current version
		changeChecker.applyToCache();
		List<SourceFeedEntry> currentFeedList = FeedReader.read(cacheUrl);
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
				oldEntries.add(cachedFeedList.get(0));
			}
			cachedFeedList.remove(0);
		}
		newEntries.addAll(currentFeedList);
	}
	
	/**
	 * manual factory method for new SourceFeedEntries that are not in DB yet
	 * 
	 * @param title			String containing the entry's title
	 * @param description	String containing the entry's description
	 * @param pubDate		Date of publication or last update
	 * @param text			String containing the full text of the entry
	 * @param URL			URL pointing to the original entry
	 * @param imgURL		URL pointing to an image belonging to the entry
	 * @return 				the newly generated SourceFeedEntry
	 */
	@Override
	public SourceFeedEntry createEntry(String title, String description, Date pubDate, String text, String URL, String imgURL) {
		return this.createEntry(0, title, description, pubDate, text, URL, imgURL);
	}

	/**
	 * manual factory method for SourceFeedEntries
	 * 
	 * @param id			ID number. Use 0 for entries that are not in database yet
	 * @param title			String containing the entry's title
	 * @param description	String containing the entry's description
	 * @param pubDate		Date of publication or last update
	 * @param text			String containing the full text of the entry
	 * @param URL			URL pointing to the original entry
	 * @param imgURL		URL pointing to an image belonging to the entry
	 * @return 				the newly generated SourceFeedEntry
	 */
	@Override
	public SourceFeedEntry createEntry(int id, String title, String description, Date pubDate, String text, String URL, String imgURL) {
		return new SourceFeedEntryRSSImpl(id, title, description, pubDate, text, URL, imgURL);
	}

	/**
	 * checks if the given URL points to a valid SourceFeedSource
	 * 
	 * @param url			URL that has to be checked
	 * @return				true if and only if this application is able to retrieve SourceFeedEntries from url
	 */
	public static boolean testURL(String url) {
		try {
			FeedReader.read(url);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
}
