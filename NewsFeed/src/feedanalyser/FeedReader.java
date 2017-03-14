package feedanalyser;

import java.net.URL;
import java.sql.Date;
import java.util.List;

import com.sun.syndication.feed.module.DCModuleImpl;
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
        boolean ok = false;
        if (true) {
            try {
                URL feedUrl = new URL("http://www.tagesschau.de/xml/rss2");

                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedUrl));
                
    
                List<SyndEntryImpl> entries = feed.getEntries();
                SyndEntryImpl entry = entries.get(0);
                
                List<DCModuleImpl> modules = entry.getModules();
                DCModuleImpl module = modules.get(0);
                Date date = (Date) module.getDate();
                
                System.out.println(feed);

                ok = true;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: "+ex.getMessage());
            }
        }

        if (!ok) {
            System.out.println();
            System.out.println("FeedReader reads and prints any RSS/Atom feed type.");
            System.out.println("The first parameter must be the URL of the feed to read.");
            System.out.println();
        }
    }

}
