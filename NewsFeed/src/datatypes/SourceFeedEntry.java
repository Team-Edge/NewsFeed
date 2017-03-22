package datatypes;

import java.util.Date;
import com.sun.syndication.feed.synd.SyndEntry;

import feedanalyser.FeedEnlarger;

public class SourceFeedEntry {

	private String title;
	private String description;
	private Date pubDate;
	private String text;
	private String URL;
	
	public SourceFeedEntry(String title, String description, Date pubDate, String text, String URL){
		
		this.title = title;
		this.description = description;
		if(pubDate != null) this.pubDate = pubDate;
		else this.pubDate = new java.util.Date();
		this.text = text;
		this.URL = URL;
	}
	
	public SourceFeedEntry(SyndEntry entry)
	{
		this.title = entry.getTitle();
		this.description = entry.getDescription().getValue();
		if(entry.getUpdatedDate() != null) 
			this.pubDate = entry.getUpdatedDate();
		else
			this.pubDate = new java.util.Date();
		this.text = null;
		this.URL = entry.getLink();
	}
	
	public void enlarge()
	{
		this.text = FeedEnlarger.getContent(this.URL);
		if(this.text == null) {
			this.text = this.description;
		}
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public String getURL() {
		return URL;
	}

	@Override
	public String toString() {
		return "Feed [URL=" + URL + "]";
	}
	
	
	
}
