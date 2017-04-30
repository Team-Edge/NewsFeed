package datatypes;

/**
 * static factory class for SourceFeeds
 */
public class SourceFeedFactory {

	/**
	 * static factory method that creates a SourceFeed using it's source URL
	 * 
	 * @param id			the database id
	 * @param url			the URL of the source
	 * @param cachefile		the filepath of the local cache file
	 * @return				SourceFeed with the given data
	 */
	public static SourceFeed createSourceFeed(int id, String url, String cachefile) {
		if(url.contains("www.facebook.com")) {
			return new SourceFeedFBImpl(id, url, cachefile);
		} else {
			return new SourceFeedRSSImpl(id, url, cachefile);
		}
	}
	
	/**
	 * checks if the given URL points to a valid SourceFeedSource
	 * 
	 * @param url			URL that has to be checked
	 * @return				true if and only if this application is able to retrieve SourceFeedEntries from url
	 */
	public static boolean testURL(String url) {
		if(url.contains("www.facebook.com")) {
			return SourceFeedFBImpl.testURL(url);
		} else {
			return SourceFeedRSSImpl.testURL(url);
		}
	}

}
