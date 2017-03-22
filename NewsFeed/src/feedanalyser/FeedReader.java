package feedanalyser;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import datatypes.SourceFeedEntry;


public class FeedReader {
    
    public static List<SourceFeedEntry> read(String url) throws IllegalArgumentException, FeedException, IOException
    {
        URL feedUrl = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed;
		URLConnection feedCon = feedUrl.openConnection();
		feedCon.setConnectTimeout(10000);
		feedCon.setReadTimeout(10000);
        try {
        	feed = input.build(new XmlReader(feedCon.getInputStream()));
        } catch(Exception e) {
        	feed = new SyndFeedImpl();
        }
        @SuppressWarnings("unchecked")
		List<SyndEntry> entries = feed.getEntries();
        List<SourceFeedEntry> ret = new LinkedList<SourceFeedEntry>();
        for(SyndEntry entry : entries) {
        	ret.add(new SourceFeedEntry(entry));
        }
        return ret;
    }

}
