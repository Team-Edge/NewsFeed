package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import feedUtils.*;
import datatypes.*;
import java.util.Date;

public class FeedUtilsTest {

	@Test
	public void testTextSearch() {
		SourceFeedEntry s = new SourceFeedEntry("Title Titel","Description Beschreibung",new Date(),"Text Text","URL URL","imgURL BildURL");
		try{
			TextSearch t = new TextSearch(s);
			try{
				t.close();
			}
			catch(Exception e)
			{
				fail("Couldn't close TextSearch");
			}
		}
		catch(Exception e)
		{
			fail("Couldn't create TextSearch");
		}
	}

}
