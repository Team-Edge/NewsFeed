package datatypes;

import java.util.Date;
import java.util.List;

/**
 * Data Container for a SourceFeed
 */
public interface SourceFeed {
	
	/**
	 * returns the SourceFeed_ID of this feed
	 * SourceFeed_ID is used as primary key for the database
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
	public SourceFeedEntry createEntry(String title, String description, Date pubDate, String text, String URL, String imgURL);
	
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
	public SourceFeedEntry createEntry(int id, String title, String description, Date pubDate, String text, String URL, String imgURL);
	
	/**
	 * fetches new SourceFeedEntries from source and identify obsolete ones from cachefile
	 * 
	 * @param newEntries	list where new SourceFeedEntries will be added
	 * @param oldEntries	list where old SourceFeedEntries will be added
	 * @throws Exception	if connection to source fails or data in source is malformed
	 */
	public void fetchEntries(List<SourceFeedEntry> newEntries, List<SourceFeedEntry> oldEntries) throws Exception;


}
