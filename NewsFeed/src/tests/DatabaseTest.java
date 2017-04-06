package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import database.*;
import program.*;
import java.sql.SQLException;
import java.io.IOException;
import java.sql.PreparedStatement;

public class DatabaseTest {

	@Test
	public void testDBConnection() {
		
		DBconnection database = null;
		try{
			database = new DBconnection("jdbc:Blablabla","Username", "Passwort");
			fail("Did not throw SQLException where expected");
			database.close();
		}catch(SQLException | IOException e)
		{
			System.out.println(e);
		}
		
		Configuration.loadFromFile("./config.txt");
		try{
			database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
			+ ":" + Configuration.getDbServerPort()
			+ "/" + Configuration.getDbServerSchemaName(), 
			Configuration.getDbServerUserName(), 
			Configuration.getDbServerPassword());
			
			PreparedStatement p = database.createStatement("NONESENSE");
			assertTrue(p.toString().contains("NONESENSE"));
			try{
				p = database.createStatement(null);
				fail("No SQLException thrown where expected");
			}catch(SQLException e)
			{
				System.out.println(e);
			}
			database.close();
			p = database.createStatement("SELECT * FROM nodb");
			try{
				database.request(p);
				fail("No SQLException thrown where expected");
			} catch(SQLException e)
			{
				System.out.println(e);
			}
			p = database.createStatement("SELECT * FROM nodb");
			try{
				database.execute(p);
				fail("No SQLException thrown where expected");
			} catch(SQLException e)
			{
				System.out.println(e);
			}
			
			
			database.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			fail("Exception was thrown");
		}
	}
	
	@Test
	public void testOrderArchiveOldSourceFeedEntries()
	{
		try{
			new OrderArchiveOldSourceFeedEntries(null);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testOrderInsertCustomFeedEntry()
	{
		try{
			new OrderInsertCustomFeedEntry(null,-1,-1);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testOrderInsertSourceFeedEntry()
	{
		try{
			new OrderInsertSourceFeedEntry(null,null,-1);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testOrderRemoveSourceFeedEntry()
	{
		try{
			new OrderRemoveSourceFeedEntry(null,null);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testQueryFilters()
	{
		try{
			new QueryFilters(null);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testQueryFilterURLs()
	{
		try{
			new QueryFilterURLs(null,-1);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testQueryFilterWords()
	{
		try{
			new QueryFilterWords(null,-1);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testQuerySourceFeeds()
	{
		try{
			new QuerySourceFeeds(null);
			fail("No Exception thrown where expected");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void testSqlOrder()
	{
		try{
			DBconnection database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
			+ ":" + Configuration.getDbServerPort()
			+ "/" + Configuration.getDbServerSchemaName(), 
			Configuration.getDbServerUserName(), 
			Configuration.getDbServerPassword());
			
			SqlOrder s = new SqlOrder(database, "? ?");
			s.setStmtString(1, "NONESENSE");
			s.setStmtInt(2, -1);
			try{
				s.setStmtString(3, "SQLSCHMARRN");
				fail("No Exception thrown where expected");
			}catch(SQLException e){
				System.out.println(e);
			}
			try{
				s.setStmtInt(3, -2);
				fail("No Exception thrown where expected");
			}catch(SQLException e){
				System.out.println(e);
			}
			try{
				s.execute();
				fail("No Exception thrown where expected");
			}catch(SQLException e){
				System.out.println(e);
			}
			
			database.close();
		}catch(Exception e)
		{
			System.out.println(e);
			fail("Exception was thrown");
		}
	}
	
	@Test
	public void testSqlQuery()
	{
		try{
			DBconnection database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
			+ ":" + Configuration.getDbServerPort()
			+ "/" + Configuration.getDbServerSchemaName(), 
			Configuration.getDbServerUserName(), 
			Configuration.getDbServerPassword());
			
			SqlQuery s = new SqlQuery(database, "? ?");
			s.setStmtString(1, "NONESENSE");
			s.setStmtInt(2, -1);
			try{
				s.setStmtString(3, "SQLSCHMARRN");
				fail("No Exception thrown where expected");
			}catch(SQLException e){
				System.out.println(e);
			}
			try{
				s.setStmtInt(3, -2);
				fail("No Exception thrown where expected");
			}catch(SQLException e){
				System.out.println(e);
			}
			try{
				s.query();
				fail("No Exception thrown where expected");
			}catch(SQLException e){
				System.out.println(e);
			}
			try{
				s.getResult();
				fail("No Exception thrown where expected");
			}catch(Exception e){
				System.out.println(e);
			}
			try{
				s.close();
				fail("No Exception thrown where expected");
			}catch(Exception e){
				System.out.println(e);
			}
			
			database.close();
		}catch(Exception e)
		{
			System.out.println(e);
			fail("Exception was thrown");
		}
	}
}
