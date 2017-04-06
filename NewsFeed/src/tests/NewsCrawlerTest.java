package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import database.DBconnection;

import java.io.File;
import java.sql.SQLException;
import datatypes.SourceFeed;
import newsCrawler.*;
import program.Configuration;

public class NewsCrawlerTest {

	@Test(expected=Exception.class)
	public void testNewsCrawler()
	{
		NewsCrawler n = new NewsCrawler();
		Configuration.loadFromFile("./TestFiles/testWrongLoginConfig.txt");
		assertTrue(n.needsDB());
		assertEquals(n.toString(),"NewsCrawler");
		n.run();
	}
	
	@Test 
	public void testNewsCrawlerUpdate()
	{
		DBconnection database = null;
		Configuration.loadFromFile("./config.txt");
		try {
			database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
			+ ":" + Configuration.getDbServerPort()
			+ "/" + Configuration.getDbServerSchemaName(), 
			Configuration.getDbServerUserName(), 
			Configuration.getDbServerPassword());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File f = new File("./TestFiles/tmpCache.txt");
		f.deleteOnExit();
		SourceFeed s = new SourceFeed(0,"file:./TestFiles/testEmptyConfig.txt","./TestFiles/tmpCache.txt");
		
		NewsCrawlerUpdate n = new NewsCrawlerUpdate(s,null);
		assertNotNull(n);
		n.run();
		n = new NewsCrawlerUpdate(s,database);
		assertNotNull(n);
		n.run();
		assertEquals(f.length(),0);
		
		s = new SourceFeed(0,"file:./TestFiles/testFeed.txt","./TestFiles/tmpCache.txt");
		n = new NewsCrawlerUpdate(s,database);
		n.run();
		assertNotEquals(f.length(),0);
	}
	
	@Test 
	public void testUpdateService()
	{
		
	}
	
	@Test 
	public void testEnlargingService()
	{
		
	}
	
	

}
