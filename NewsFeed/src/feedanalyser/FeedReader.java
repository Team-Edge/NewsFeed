package feedanalyser;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;


public class FeedReader {
    
    public static List<Feed> read(String url) throws IllegalArgumentException, FeedException, IOException
    {
        URL feedUrl = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
        try {
        	feed = input.build(new XmlReader(feedUrl));
        } catch(Exception e) {
        	feed = new SyndFeedImpl();
        }
        @SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
        List<Feed> ret = new LinkedList<Feed>();
        for(SyndEntry entry : entries) {
        	ret.add(new Feed(entry));
        }
        return ret;
    }

}
