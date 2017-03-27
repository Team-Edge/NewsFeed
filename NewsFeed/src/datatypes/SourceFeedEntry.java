package datatypes;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.syndication.feed.synd.SyndEntry;

import feedanalyser.FeedEnlarger;

public class SourceFeedEntry {

	private String title;
	private String description;
	private Date pubDate;
	private String text;
	private String URL;
	private String imgURL;
	
	public SourceFeedEntry(String title, String description, Date pubDate, String text, String URL, String imgURL) {
		this.title = title;
		this.description = description;
		if(pubDate != null) this.pubDate = pubDate;
		else this.pubDate = new java.util.Date();
		this.text = text;
		this.URL = URL;
		this.imgURL = imgURL;
	}
	
	public SourceFeedEntry(SyndEntry entry) {
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
	
	public void enlarge() {
		this.text = FeedEnlarger.getContent(this.URL);
		if(this.text == null) {
			this.text = this.description;
		}
		
		 Pattern p = Pattern.compile("<img src=\"(\\S+)\"(\\p{ASCII})*/>");
		 Matcher m = p.matcher(this.text);
		 if (m.find()) {
			 this.imgURL = m.group(1);
		 }
		 else {
			 this.imgURL = "";
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

	public String getImgURL() {
		return imgURL;
	}

	@Override
	public String toString() {
		return "Feed [URL=" + URL + "]";
	}
	
	
}
