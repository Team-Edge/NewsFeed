package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import com.sun.syndication.io.FeedException;

import feedUtils.*;
import datatypes.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

public class FeedUtilsTest {

	@Test
	public void testTextSearch() {
		SourceFeedEntry s = new SourceFeedEntryRSSImpl("Title Titel","Description Beschreibung",new Date(),"Text Text","URL URL","imgURL BildURL");
		List<String> titleWords = new ArrayList<String>();
		List<String> descrWords = new ArrayList<String>();
		List<String> textWords = new ArrayList<String>();
		List<String> invTitleWords = new ArrayList<String>();
		List<String> invDescrWords = new ArrayList<String>();
		List<String> invTextWords = new ArrayList<String>();
		
		try{
			TextSearch t = new TextSearch(s);
			/*Test with empty Lists */
			assertTrue(t.query(titleWords, descrWords, textWords, invTitleWords, invDescrWords, invTextWords));
			
			/*Test for success*/
			titleWords.add("Titel");
			titleWords.add("Title");
			descrWords.add("Beschreibung");
			descrWords.add("Description");
			textWords.add("Text");
			invTitleWords.add("NotTitle");
			invDescrWords.add("NotDescr");
			invTextWords.add("NotText");
			assertTrue(t.query(titleWords, descrWords, textWords, invTitleWords, invDescrWords, invTextWords));
			
			/*Test for failure*/
			titleWords.add("NotTitle");
			descrWords.add("notDescr");
			textWords.add("NotText");
			invTitleWords.add("Titel");
			invDescrWords.add("Beschreibung");
			invTextWords.add("Text");
			
			assertFalse(t.query(titleWords, descrWords, textWords, invTitleWords, invDescrWords, invTextWords));
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
	
	@Test
	public void testFeedReader()
	{
		/*
		 * Test for IOException
		 */
		try {
			FeedReader.read("ThisIsNoURL");
			fail("FeedReader did not throw Exception where expected");
		} catch (IllegalArgumentException e) {
			System.out.println(e);
			fail("Illegal Argument Exception was thrown instead of IOException");
		} catch (FeedException e) {
			System.out.println(e);
			fail("Feed Exception was thrown instead of IOException");
		} catch (IOException e) {
			System.out.println(e);
		}
		
		/*
		 * Test for intern catch
		 */
		try {
			List<SourceFeedEntry> sf = new LinkedList<SourceFeedEntry>();
			sf=FeedReader.read("file:///ThisIsNoURL");
			assertTrue(sf.isEmpty());
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		} catch (FeedException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		/*
		 * Test for success
		 */
		try {
			List<SourceFeedEntry> sf = new LinkedList<SourceFeedEntry>();
			sf=FeedReader.read("file:./TestFiles/testFeed.txt");
			assertFalse(sf.isEmpty());
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		} catch (FeedException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	@Test
	public void testFeedEnlarger()
	{
		/*
		 * Test for failure (URL does not exist): String must be null
		 */
		String s1;
		s1=FeedEnlarger.getContent("http://ThisIsNoURL");
		assertNull(s1);
		
		/*
		 * Test for failure (Argument is null): String must be null
		 */
		String s2;
		s2=FeedEnlarger.getContent(null);
		assertNull(s2);
		
		/*
		 * Test for success: String must not be empty
		 */
		String s3;
		s3=FeedEnlarger.getContent("https://news.google.de/news?cf=all&hl=de&pz=1&ned=de&output=rss");	//Using rss feed of google: shouldn't ever become a dead link
		assertFalse(s3.isEmpty());
	}
	
	@Test
	public void testCacheUpdate()
	{
		/*
		 * Creating two Files for testing
		 */
		File f = new File("./TestFiles/tmpCache.txt");	//Creates temporary cachefile (empty)
		try {
			f.createNewFile();
		} catch (IOException e1) {
			System.out.println(e1);
			fail("Could not create tmpCache.txt");
		}
		f.deleteOnExit();
		File emptyFeed = new File("./TestFiles/emptyFeed.txt");	//creates empty Feed file
		try {
			emptyFeed.createNewFile();
		} catch (IOException e1) {
			System.out.println(e1);
			fail("Could not create emptyFeed.txt");
		}
		emptyFeed.deleteOnExit();
		CacheUpdate cu = null;
		
		/*
		 * Test with two empty files
		 */
		try {
			cu = new CacheUpdate("file:./TestFiles/emptyFeed.txt","./TestFiles/tmpCache.txt");
		} catch (Exception e) {
			System.out.println(e);
			fail("Failed to use diff");
		}
		assertFalse(cu.hasInsertions());
		assertFalse(cu.hasDeletions());
		assertFalse(cu.hasChanges());
		try {
			cu.applyToCache();
		} catch (Exception e) {
			System.out.println(e);
			fail("applyToCache() failed");
		}
		
		/*
		 * Testing insertion from full Feed file into empty Cachefile
		 */
		try {
			cu = new CacheUpdate("file:./TestFiles/testFeed.txt","./TestFiles/tmpCache.txt");
		} catch (Exception e) {
			System.out.println(e);
			fail("Failed to use diff");
		}
		assertTrue(cu.hasInsertions());
		assertFalse(cu.hasDeletions());
		assertTrue(cu.hasChanges());
		try {
			cu.applyToCache();
		} catch (Exception e) {
			System.out.println(e);
			fail("applyToCache() failed");
		}
		
		/*
		 * Test if feeds have been successfully written into cachefile
		 */
		try {
			cu = new CacheUpdate("file:./TestFiles/testFeed.txt","./TestFiles/tmpCache.txt");
		} catch (Exception e) {
			System.out.println(e);
			fail("Failed to use diff");
		}
		assertFalse(cu.hasInsertions());
		assertFalse(cu.hasDeletions());
		assertFalse(cu.hasChanges());
		try{
			cu.applyToCache();
		} catch (Exception e) {
			System.out.println(e);
			fail("applyToCache() failed");
		}
		
		/*
		 * Test with empty Feed file -> Deletion of all data out of cachefile
		 */
		try {
			cu = new CacheUpdate("file:./TestFiles/emptyFeed.txt","./TestFiles/tmpCache.txt");
		} catch (Exception e) {
			System.out.println(e);
			fail("Failed to use diff");
		}
		assertFalse(cu.hasInsertions());
		assertTrue(cu.hasDeletions());
		assertTrue(cu.hasChanges());
		try{
			cu.applyToCache();
		} catch (Exception e) {
			System.out.println(e);
			fail("applyToCache() failed");
		}
		assertEquals(f.length(),0);
		
		/*
		 * Test with null arguments
		 */
		CacheUpdate cu2 = null;
		try {
			cu2 = new CacheUpdate(null,null);
		} catch (Exception e) {
			System.out.println(e);
		}
		assertNull(cu2);
	}

}
