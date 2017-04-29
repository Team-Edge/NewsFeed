package datatypes;

import java.util.Date;
import java.util.List;

/**
 * Data Container for a SourceFeed
 */
public interface SourceFeed {
	
	/**
	 * returns the SourceFeed_ID of this feed
	 * SourceFeed_ID is used as primary key for the 
	 * @return the SourceFeed_ID of this feed
	 */
	public int getID();

	/**
	 * returns the URL of this SourceFeed
	 * @return the URL of this SourceFeed
	 */
	public String getUrl();

	/**
	 * returns the path of the local cachefile
	 * @return the path of the local cachefile
	 */
	public String getCache();
	
	
	public SourceFeedEntry createEntry(String title, String description, Date pubDate, String text, String URL, String imgURL);
	
	
	public SourceFeedEntry createEntry(int id, String title, String description, Date pubDate, String text, String URL, String imgURL);
	
	
	public void fetchEntries(List<SourceFeedEntry> newEntries, List<SourceFeedEntry> oldEntries) throws Exception;


}
