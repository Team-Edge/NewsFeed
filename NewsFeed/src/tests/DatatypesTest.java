package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import datatypes.*;

public class DatatypesTest {

	@Test
	public void testSourceFeed() {
		SourceFeed s = new SourceFeedRSSImpl(100,"urlFilePath","cacheFilePath");
		assertEquals(s.getID(),100);
		assertEquals(s.getUrl(),"urlFilePath");
		assertEquals(s.getCache(),"cacheFilePath");
	}
	
	@Test
	public void testSourceFeedEntry()
	{
		SourceFeedEntry s = new SourceFeedEntryRSSImpl("Titel", "Beschreibung", null, "Text", "URL", "BildURL");
		assertEquals(s.getTitle(),"Titel");
		assertEquals(s.getDescription(),"Beschreibung");
		assertEquals(s.getPubDate(),new java.util.Date());
		assertEquals(s.getText(),"Text");
		assertEquals(s.getURL(),"URL");
		assertEquals(s.getImgURL(),"BildURL");
		assertEquals(s.toString(),"Feed [URL=URL]");
		
		java.util.Date d = new java.util.Date();
		s = new SourceFeedEntryRSSImpl("Title", "Description", d, "text", "URL2", "ImageURL");
		assertEquals(s.getTitle(),"Title");
		assertEquals(s.getDescription(),"Description");
		assertEquals(s.getPubDate(),d);
		assertEquals(s.getText(),"text");
		assertEquals(s.getURL(),"URL2");
		assertEquals(s.getImgURL(),"ImageURL");
		assertEquals(s.toString(),"Feed [URL=URL2]");
		
		s.enlarge();
		assertEquals(s.getDescription(),"Description");
		assertEquals(s.getText(),"Description");
		assertEquals(s.getImgURL(),"");
	}

}
