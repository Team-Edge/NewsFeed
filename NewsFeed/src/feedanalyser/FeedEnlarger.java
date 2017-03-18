/**
 * 
 */
package feedanalyser;


import java.net.URLEncoder;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Florian
 *
 */
public class FeedEnlarger {
	
	public static void main(String[] args)
	{
		String url = "http://www.tagesschau.de/ausland/secretservice-103.html";
		System.out.println(url);
		System.out.println(getContent(url));
	}
	
	public static String getContent(String url)
	{
		String ret = requestFiveFilters(url);
		if(ret==null) ret = requestFeedEnlarger(url);
		return ret;
	}
	
	private static String requestFiveFilters(String url)
	{
		try {
			String feedUrl = "http://ftr.fivefilters.org/makefulltextfeed.php?url=";
			feedUrl += URLEncoder.encode(url, "UTF-8");
			feedUrl += "&max=3";
			String fulltext = FeedReader.read(feedUrl).get(0).getDescription();
			return StringEscapeUtils.unescapeHtml4(fulltext);
		} catch (Exception e) {
			return null;
		}		
	}
	
	private static String requestFeedEnlarger(String url)
	{
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
