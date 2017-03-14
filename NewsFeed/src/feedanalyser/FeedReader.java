package feedanalyser;

import java.net.URL;
//import java.sql.Date;
import java.util.Date;
import java.util.List;

import com.sun.syndication.feed.module.DCModule;
import com.sun.syndication.feed.module.DCModuleImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

/**
 * It Reads and prints any RSS/Atom feed type.
 * <p>
 * @author Alejandro Abdelnur
 *
 */
public class FeedReader {

    public static void main(String[] args) {
        try {
        	// !!! Different behavior for different feeds!
        	// http://www.tagesschau.de/xml/rss2
        	// http://www.tagesschau.de/newsticker.rdf
            URL feedUrl = new URL("http://www.tagesschau.de/xml/atom/");

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            

            List<SyndEntry> entries = feed.getEntries();
            SyndEntry entry = entries.get(0);
            
            List<DCModule> modules = entry.getModules();
            DCModule module = modules.get(0);
            Date date = (Date) module.getDate();           
            System.out.println("Date from modules: " + date);
            
            System.out.println("Date from entry directly: " + entry.getUpdatedDate());
            
            System.out.println(feed);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }
    }

}
