package datatypes;

import java.util.Date;

public interface SourceFeedEntry {

	/**
	 * enlarges this SourceFeedEntry
	 * which means that a full text and an image will be added
	 * @see feedUtils.FeedEnlarger
	 */
	public void enlarge();

	/**
	 * returns the ID of this SourceFeedEntry
	 * @return the ID of this SourceFeedEntry or 0 if not set
	 */
	public int getID();
	
	/**
	 * returns the full text of this SourceFeedEntry
	 * @return the full text of this SourceFeedEntry
	 */
	public String getText();

	/**
	 * returns the title of this SourceFeedEntry
	 * @return the title of this SourceFeedEntry
	 */
	public String getTitle();

	/**
	 * returns the description of this SourceFeedEntry
	 * @return the description of this SourceFeedEntry
	 */
	public String getDescription();

	/**
	 * returns the publication/update date of this SourceFeedEntry
	 * @return the publication/update date of this SourceFeedEntry
	 */
	public Date getPubDate();

	/**
	 * returns the URL of this SourceFeedEntry
	 * @return the URL of this SourceFeedEntry
	 */
	public String getURL();

	/**
	 * returns the image-URL of this SourceFeedEntry
	 * @return the image-URL of this SourceFeedEntry
	 */
	public String getImgURL();

	@Override
	public String toString();
	
}
