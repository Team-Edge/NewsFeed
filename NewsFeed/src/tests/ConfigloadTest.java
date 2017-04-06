package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import configload.*;
import program.Configuration;

public class ConfigloadTest {

	@Test
	public void testConfigLoad()
	{
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
	}

}
