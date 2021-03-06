package datatypes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Post;
import facebook4j.Reading;
import facebook4j.ResponseList;
import program.Configuration;

/**
 * Data Container for a facebook SourceFeed
 */
class SourceFeedFBImpl implements SourceFeed {
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
	public SourceFeedFBImpl(int id, String url, String cache) {
		this.id = id;
		this.url = url;
		this.cache = cache;
	}

	/**
	 * returns the SourceFeed_ID of this feed
	 * SourceFeed_ID is used as primary key for the database
	 * @return the SourceFeed_ID of this feed
	 */
	@Override
	public int getID() {
		return id;
	}

	/**
	 * returns the URL of this SourceFeed
	 * @return the URL of this SourceFeed
	 */
	@Override
	public String getUrl() {
		return url;
	}

	/**
	 * returns the path of the local cachefile
	 * @return the path of the local cachefile
	 */
	@Override
	public String getCache() {
		return cache;
	}

	/**
	 * fetches new SourceFeedEntries from source and identify obsolete ones from cachefile
	 * 
	 * @param newEntries	list where new SourceFeedEntries will be added
	 * @param oldEntries	list where old SourceFeedEntries will be added
	 * @throws Exception	if connection to source fails or data in source is malformed
	 */
	@Override
	public void fetchEntries(List<SourceFeedEntry> newEntries, List<SourceFeedEntry> oldEntries) throws Exception {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(Configuration.getFbAppID(), Configuration.getFbAppSecret());
		facebook.setOAuthPermissions("public_profile");
		
		try {
			//this is not a joke!
			facebook.setOAuthAccessToken(facebook.getOAuthAppAccessToken());
			//extract the page Identifier from URL
			URI sourcePage = new URI(this.url);
			String path = sourcePage.getPath();
			if(path.endsWith("/"))
				path = path.substring(0, path.length()-1);
			String pageIdentifier = path.substring(path.lastIndexOf('/')+1);
			//picture of page(if existing) is standard picture for posts without pictures
			String stdpic="";
			try {
				URL stdpicUrl = facebook.getPage(pageIdentifier).getPicture();
				if(stdpicUrl!=null)
					stdpic=stdpicUrl.toString();
			} catch(Exception e) {
				//ignore
			}
			//Settings for reading posts
			Reading settings = new Reading().fields("full_picture", "name", "description", "link", "permalink_url", "from");
			//get id and date of the last processed post
			File cacheFile = new File(this.cache);
			Date lastUpdate;
			String lastPost="";
			if(cacheFile.isFile()) {
				try (
					FileReader fr = new FileReader(cacheFile);
				    BufferedReader br = new BufferedReader(fr);
				) {
				    lastPost = br.readLine();
				    SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getFbCacheDateFormat());
				    lastUpdate = sdf.parse(br.readLine());
				    settings=settings.since(lastUpdate);
				} catch(Exception e) {
					//regard invalid file as non-existent
					settings=settings.limit(20);
				}
			} else {
				settings=settings.limit(20);
			}
			//get posts, convert them to SourceFeedEntries and add them to lists
			ResponseList<Post> articles = facebook.getFeed(pageIdentifier, settings);
			if(!articles.isEmpty()) {
				String newestPost=articles.get(0).getId();
				for(Post article : articles) {
					if(article.getId()==lastPost) {
						break; 
					}
					newEntries.add(new SourceFeedEntryFBImpl(article, stdpic));
				}
				try (
					FileWriter fw = new FileWriter(cacheFile);
				    BufferedWriter bw = new BufferedWriter(fw);
				) {
					bw.write(newestPost);
				    bw.newLine();
				    SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getFbCacheDateFormat());
				    bw.write(sdf.format(Calendar.getInstance().getTime()));
				    bw.newLine();
				} catch(Exception e) {
					//regard invalid file as non-existent
				}
			} 
		} catch (Exception e) {
			throw(e);
		} finally {
			//each entry will be immediately marked as 'toRemove'
			//this shortens lifetime, but is fail-safe and avoids local logging of json responses
			oldEntries.addAll(newEntries);
		}
	}

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
	@Override
	public SourceFeedEntry createEntry(String title, String description, Date pubDate, String text, String URL, String imgURL) {
		return this.createEntry(0, title, description, pubDate, text, URL, imgURL);
	}

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
	@Override
	public SourceFeedEntry createEntry(int id, String title, String description, Date pubDate, String text, String URL, String imgURL) {
		return new SourceFeedEntryFBImpl(id, title, description, pubDate, text, URL, imgURL);
	}

	/**
	 * checks if the given URL points to a valid SourceFeedSource
	 * 
	 * @param url			URL that has to be checked
	 * @return				true if and only if this application is able to retrieve SourceFeedEntries from url
	 */
	public static boolean testURL(String url) {
		Facebook facebook = new FacebookFactory().getInstance();
		facebook.setOAuthAppId(Configuration.getFbAppID(), Configuration.getFbAppSecret());
		facebook.setOAuthPermissions("public_profile");
		try {
			//this is not a joke!
			facebook.setOAuthAccessToken(facebook.getOAuthAppAccessToken());
			//extract the page Identifier from URL
			URI sourcePage = new URI(url);
			String path = sourcePage.getPath();
			if(path.endsWith("/"))
				path = path.substring(0, path.length()-1);
			String pageIdentifier = path.substring(path.lastIndexOf('/')+1);
			facebook.getPage(pageIdentifier).getId();
			Reading settings = new Reading().limit(1);
			facebook.getFeed(pageIdentifier, settings);
			return true;
		} catch(Exception e) {
			return false;
		}
	}

}
