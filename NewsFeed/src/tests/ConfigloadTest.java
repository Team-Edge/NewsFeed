package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import configload.*;
import program.Configuration;

public class ConfigloadTest {

	@Test
	public void testConfigLoad()
	{
		//Test for right loading
		ConfigLoad cl = new ConfigLoad("./TestFiles/TestErrorConfig.txt");
		assertEquals(cl.toString(),"ConfigLoad");
		assertFalse(cl.needsDB());
		cl.run();
		assertEquals(Configuration.getDbServerHostName(), "127.0.0.2");
		assertEquals(Configuration.getDbServerPort(), "3305");
		assertEquals(Configuration.getDbServerSchemaName(),"SchemaName");
		assertEquals(Configuration.getDbServerUserName(), "UserName");
		assertEquals(Configuration.getDbServerPassword(), "Password");
		assertEquals(Configuration.getDbSqlDateFormat(),"yyyy-MM-aa HH:mm:bb");
		assertEquals(Configuration.getNetworkConnectionTimeout(),10);
		assertEquals(Configuration.getNetworkReadTimeout(),10);
		assertTrue(Configuration.getServiceInfiniteLoop());
		assertEquals(Configuration.getServiceCyclicDelay(),10);
		assertTrue(Configuration.getGeneralBeVerbose());
		assertEquals(Configuration.getGeneralOutputDateFormat(),"aa.MM.yyyy HH:mm:bb");
		
		//Test for null
		cl = new ConfigLoad(null);
		cl.run();
		assertEquals(Configuration.getDbServerHostName(), "127.0.0.1");
		assertEquals(Configuration.getDbServerPort(), "3306");
		assertEquals(Configuration.getDbServerSchemaName(),"Newsfeed");
		assertEquals(Configuration.getDbServerUserName(), "root");
		assertEquals(Configuration.getDbServerPassword(), "");
		assertEquals(Configuration.getDbSqlDateFormat(),"yyyy-MM-dd HH:mm:ss");
		assertEquals(Configuration.getNetworkConnectionTimeout(),10000);
		assertEquals(Configuration.getNetworkReadTimeout(),10000);
		assertFalse(Configuration.getServiceInfiniteLoop());
		assertEquals(Configuration.getServiceCyclicDelay(),1000);
		assertFalse(Configuration.getGeneralBeVerbose());
		assertEquals(Configuration.getGeneralOutputDateFormat(),"dd.MM.yyyy HH:mm:ss");
	}

}
