package datatypes;

/**
 * Data Container for a SourceFeed
 */
public class SourceFeed {
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
	public SourceFeed(int id, String url, String cache) {
		this.id = id;
		this.url = url;
		this.cache = cache;
	}

	/**
	 * returns the SourceFeed_ID of this feed
	 * SourceFeed_ID is used as primary key for the 
	 * @return the SourceFeed_ID of this feed
	 */
	public int getID() {
		return id;
	}

	/**
	 * returns the URL of this SourceFeed
	 * @return the URL of this SourceFeed
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * returns the path of the local cachefile
	 * @return the path of the local cachefile
	 */
	public String getCache() {
		return cache;
	}

}
