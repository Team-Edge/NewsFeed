package datatypes;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.syndication.feed.synd.SyndEntry;

import feedUtils.FeedEnlarger;

/**
 * data container for a entry of a feed 
 */
public class SourceFeedEntryRSSImpl implements SourceFeedEntry {
	private int ID;
	private String title;
	private String description;
	private Date pubDate;
	private String text;
	private String URL;
	private String imgURL;
	
	/**
	 * manual constructor for new SourceFeedEntries that are not in DB yet
	 * 
	 * @param title			String containing the entry's title
	 * @param description	String containing the entry's description
	 * @param pubDate		Date of publication or last update
	 * @param text			String containing the full text of the entry
	 * @param URL			URL pointing to the original entry
	 * @param imgURL		URL pointing to an image belonging to the entry
	 */
	public SourceFeedEntryRSSImpl(String title, String description, Date pubDate, String text, String URL, String imgURL) {
		this(0, title, description, pubDate, text, URL, imgURL);
	}
	
	/**
	 * manual constructor for SourceFeedEntries
	 * 
	 * @param id			ID number. Use 0 for entries that are not in database yet
	 * @param title			String containing the entry's title
	 * @param description	String containing the entry's description
	 * @param pubDate		Date of publication or last update
	 * @param text			String containing the full text of the entry
	 * @param URL			URL pointing to the original entry
	 * @param imgURL		URL pointing to an image belonging to the entry
	 */
	public SourceFeedEntryRSSImpl(int id, String title, String description, Date pubDate, String text, String URL, String imgURL) {
		this.ID = id;
		this.title = title;
		this.description = description;
		if(pubDate != null) {
			this.pubDate = pubDate;
		} else {
			this.pubDate = new java.util.Date();
		}
		this.text = text;
		this.URL = URL;
		this.imgURL = imgURL;
	}
	
	/**
	 * automatic constructor
	 * 
	 * @param entry			SyndEntry containing data with which this entry will be created
	 */
	public SourceFeedEntryRSSImpl(SyndEntry entry) {
		this.ID = 0;
		try {
			this.title = entry.getTitle().trim();
		} catch (Exception e) {
			this.title = null;
		}
		
		try {
			this.description = entry.getDescription().getValue().trim();
		} catch (Exception e) {
			this.description = null;
		}
		if(this.description == null) {
			this.description = this.title;
		}
		else if(this.description.trim().isEmpty()) {
			this.description = this.title;
		}
		
		if(entry.getUpdatedDate() != null) {
			this.pubDate = entry.getUpdatedDate();
		} else {
			this.pubDate = new java.util.Date();
		}
		
		this.URL = entry.getLink();
		
		this.text = null;
		this.imgURL = null;
	}
	
	/**
	 * enlarges this SourceFeedEntry
	 * which means that a full text and an image will be added
	 * @see feedUtils.FeedEnlarger
	 */
	public void enlarge() {
		this.text = FeedEnlarger.getContent(this.URL);
		if(this.text == null) {
			this.text = this.description;
		}
		
		try{
			 Pattern p = Pattern.compile("<img src=\"(\\S+)\"(\\p{ASCII})*/>");
			 Matcher m = p.matcher(this.text);
			 if (m.find()) {
				 this.imgURL = m.group(1);
			 } else {
				 this.imgURL = "";
			 }
			 m = p.matcher(this.description);
			 if (m.find()) {
				 if(this.imgURL == "") {
					 this.imgURL = m.group(1);
				 }
				 this.description = m.replaceAll("");
			 }

		} catch (NullPointerException e)
		{
			System.out.println(e);
		}
	}

	/**
	 * returns the ID of this SourceFeedEntry
	 * @return the ID of this SourceFeedEntry or 0 if not set
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * returns the full text of this SourceFeedEntry
	 * @return the full text of this SourceFeedEntry
	 */
	public String getText() {
		return text;
	}

	/**
	 * returns the title of this SourceFeedEntry
	 * @return the title of this SourceFeedEntry
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * returns the description of this SourceFeedEntry
	 * @return the description of this SourceFeedEntry
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * returns the publication/update date of this SourceFeedEntry
	 * @return the publication/update date of this SourceFeedEntry
	 */
	public Date getPubDate() {
		return pubDate;
	}

	/**
	 * returns the URL of this SourceFeedEntry
	 * @return the URL of this SourceFeedEntry
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * returns the image-URL of this SourceFeedEntry
	 * @return the image-URL of this SourceFeedEntry
	 */
	public String getImgURL() {
		return imgURL;
	}

	@Override
	public String toString() {
		return "Feed [URL=" + URL + "]";
	}
	
	
}
