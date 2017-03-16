package feedanalyser;

import java.util.Date;

public class Feed {

	private String title;
	private String description;
	private Date pubDate;
	private String text;
	private String URL;
	
	public Feed(String title, String description, Date pubDate, String text, String URL){
		
		this.title = title;
		this.description = description;
		if(pubDate != null) this.pubDate = pubDate;
		else this.pubDate = new java.util.Date();
		this.text = text;
		this.URL = URL;
	}
}
