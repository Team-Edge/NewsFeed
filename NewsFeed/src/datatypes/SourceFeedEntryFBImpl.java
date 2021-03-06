package datatypes;

import java.util.Calendar;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;

import facebook4j.Post;

/**
 * data container for a entry of a Facebook feed 
 */
public class SourceFeedEntryFBImpl implements SourceFeedEntry {
	private int ID;
	private String title;
	private String description;
	private Date pubDate;
	private String text;
	private String URL;
	private String imgURL;
	private String fbID;

	
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
	public SourceFeedEntryFBImpl(String title, String description, Date pubDate, String text, String URL, String imgURL) {
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
	public SourceFeedEntryFBImpl(int id, String title, String description, Date pubDate, String text, String URL, String imgURL) {
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
	 * @param article		facebook Post from that the entry will be generated
	 */
	public SourceFeedEntryFBImpl(Post article) {
		this(article, "");
	}
	
	/**
	 * automatic constructor with standard picture
	 * 
	 * @param article		facebook Post from that the entry will be generated
	 * @param stdpic		standard picture that is used if no picture is present in article
	 */
	public SourceFeedEntryFBImpl(Post article, String stdpic) {
		this.ID=0;		
		this.fbID = article.getId();
		//title
		if(article.getStory()!=null) {
			this.title = article.getStory();
		} else if(article.getFrom()!=null) {
			this.title = "New Post from " + article.getFrom().getName();
		} else {
			this.title = "New Post";
			if(article.getName()!=null) {
				this.title += ": "+article.getName();
			}
		}
		//description
		if(article.getMessage()!=null) {
			this.description = article.getMessage();
		} else if(article.getDescription()!=null) {
			this.description = article.getDescription();
		} else {
			this.description = this.title;
		}	
		this.text=this.description;
		//URL
		if(article.getPermalinkUrl() != null) {
			this.URL = article.getPermalinkUrl().toString();
		} else if(article.getLink() != null) {
			this.URL = article.getLink().toString();
		} else {
			this.URL = "www.facebook.com";
		}
		//pubDate
		if(article.getCreatedTime()!=null) {
			this.pubDate = article.getCreatedTime();
		} else if(article.getUpdatedTime()!=null) {
			this.pubDate = article.getUpdatedTime();
		} else {
			this.pubDate = Calendar.getInstance().getTime();
		}
		//ImgURL
		if(article.getFullPicture()!=null) {
			this.imgURL = article.getFullPicture().toString();
		} else {
			this.imgURL = stdpic;
		}
	}

	/**
	 * enlarges this SourceFeedEntry
	 * which means that a full text and an image will be added
	 * @see feedUtils.FeedEnlarger
	 */
	@Override
	public void enlarge() {
		this.cleanHtmlTags();
	}
	
	/**
	 * removes all html tags from the SourceFeedEntry
	 */
	private void cleanHtmlTags() {
		this.title = new HtmlToPlainText().getPlainText(Jsoup.parse(this.title));
		this.description = new HtmlToPlainText().getPlainText(Jsoup.parse(this.description));
		this.text = new HtmlToPlainText().getPlainText(Jsoup.parse(this.text));
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
	
	/**
	 * returns the Facebook API ID of the corresponding Post of this SourceFeedEntry
	 * @return the Facebook API ID of the corresponding Post of this SourceFeedEntry
	 */
	public String getFbID() {
		return fbID;
	}

	@Override
	public String toString() {
		return "Feed [URL=" + URL + "]";
	}
}
