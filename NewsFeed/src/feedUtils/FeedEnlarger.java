/**
 * 
 */
package feedUtils;


import java.net.URLEncoder;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * static class providing functions to receive full content texts from URLs
 */
public class FeedEnlarger {
	
	/**
	 * returns the full content text from a given URL without unnecessary page contents
	 * @param url	specifies the URL from where the contents have to be received
	 * @return	the full text or null
	 */
	public static String getContent(String url) {
		String ret = requestFiveFilters(url);
		if(ret==null) {
			ret = requestFeedEnlarger(url);
		}
		return ret;
	}
	
	/**
	 * determines the full text using FiveFilters
	 * @param url	the URL from where the contents have to be received
	 * @return	the full text or null
	 */
	private static String requestFiveFilters(String url) {
		try {
			String feedUrl = "http://ftr.fivefilters.org/makefulltextfeed.php?url=";
			feedUrl += URLEncoder.encode(url, "UTF-8");
			feedUrl += "&max=3";
			String fulltext = FeedReader.read(feedUrl).get(0).getDescription();
			fulltext = StringEscapeUtils.unescapeHtml4(fulltext);
			if(fulltext.contains("[unable to retrieve full-text content]")) {
				return null;
			} else {
				return fulltext;
			}
		} catch (Exception e) {
			return null;
		}		
	}
	
	/**
	 * determines the full text using FeedEnlarger
	 * @param url the URL from where the contents have to be received
	 * @return	the full text or null
	 */
	private static String requestFeedEnlarger(String url) {
		try {
			String feedUrl = "http://feedenlarger.com/makefulltextfeed.php?url=";
			feedUrl += URLEncoder.encode(url, "UTF-8");
			feedUrl += "&max=5&links=preserve&exc=&submit=Create+full+text+feed";
			String fulltext = FeedReader.read(feedUrl).get(0).getDescription();
			return StringEscapeUtils.unescapeHtml4(fulltext);
		} catch (Exception e) {
			return null;
		}		
	}
	

}
