
package feedUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import datatypes.SourceFeedEntry;
import datatypes.SourceFeedEntryRSSImpl;
import program.Configuration;

/**
 * static class to parse a RSS- or Atomfeed to a list of SourceFeedEntry-Containers
 */
public class FeedReader {
    
	/**
	 * cannot throw FeedException or IllegalArgumentException because they are catched intern
	 * 
	 * parses a RSS- or Atomfeed to a list of SourceFeedEntry-Containers
	 * @param url	URL to the feed adress
	 * @return 	a list of SourceFeedEntry-Containers
	 * @throws IllegalArgumentException if the url does no point ro a supported syndication feed
	 * @throws FeedException if the feed could not be read
	 * @throws IOException if the Feed could not be opened
	 */
    public static List<SourceFeedEntry> read(String url) throws IllegalArgumentException, FeedException, IOException {
        URL feedUrl = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
		URLConnection feedCon = feedUrl.openConnection();
		feedCon.setConnectTimeout(Configuration.getNetworkConnectionTimeout());
		feedCon.setReadTimeout(Configuration.getNetworkReadTimeout());
        try {
        	feed = input.build(new XmlReader(feedCon.getInputStream()));
        } catch(Exception e) {
        	SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
        	Calendar cal = Calendar.getInstance();
        	System.err.println(sdf.format(cal.getTime()) + " : Error while fetching RSS feed");
			System.err.println(e.getMessage());
			System.err.println();
        	feed = new SyndFeedImpl();
        }
        @SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
        List<SourceFeedEntryRSSImpl> ret = new LinkedList<SourceFeedEntryRSSImpl>();
        for(SyndEntry entry : entries) {
        	SourceFeedEntryRSSImpl nextone = new SourceFeedEntryRSSImpl(entry);
        	//filter out duplicate entries
        	//greetings to FOCUS ONLINE %@&§#!!!
        	boolean toadd = true;
        	for(SourceFeedEntryRSSImpl current : ret) {
        		if(current.getURL().equalsIgnoreCase(nextone.getURL())) {
        			toadd=false;
        			break;
        		}
        	}
        	if(toadd) {
        		ret.add(nextone);
        	}
        }
        return new LinkedList<SourceFeedEntry>(ret);
    }

}
