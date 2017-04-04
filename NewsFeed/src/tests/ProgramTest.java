package tests;

import static org.junit.Assert.*;
import org.junit.Test;
import program.*;

public class ProgramTest {
	
	/*
	 * Only calling main() function with certain arguments that will throw exceptions or call the help-text.
	 * This causes no interaction with the database
	 */
	@Test
	public void testMain() {
		testMainNoArgs();
		testMainUnknownArg();
		testMainHelp();
		testMainWrongConfigFile();
		testMainConfigFile();
	}
	
	/*
	 * Test: Calls main() without arguments
	 */
	private void testMainNoArgs()
	{
		String[] args = null;
		Program.main(args);
		
		String[] args2 = {""};
		Program.main(args2);
		
		String[] args3 = {};
		Program.main(args3);
	}
	
	/*
	 * Test: Calls main() with unknown Argument
	 */
	private void testMainUnknownArg()
	{
		String[] args = {"unknownArg"};
		Program.main(args);
	}
	
	/*
	 * Test: Calls main() with arguments "--help" and "-?" to print the help-text
	 */
	private void testMainHelp()
	{
		String[] args = {"--help", "-?"};
		Program.main(args);
	}
	
	private void testMainWrongConfigFile()
	{
		/*Test invalid config file path */
		String[] args = {"-c", "noSuchFile.txt"};
		Program.main(args);
		args[0] = "--config";
		Program.main(args);
		
		/*Test missing config file path */
		String[] args2 = {"-c"};
		Program.main(args2);
		args2[0] = "--config";
		Program.main(args2);
	}
	
	private void testMainConfigFile()
	{
		/*Test invalid config file path */
		String[] args = {"-c", "./TestFiles/testWrongLoginConfig.txt"};
		Program.main(args);
		args[0] = "--config";
		Program.main(args);
	}
	
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
	public void testHelpTextOutput()
	{
		HelpTextOutput h = new HelpTextOutput();
		assertFalse(h.needsDB());
		assertEquals(h.toString(),"HelpTextOutput");
	}
	
	@Test
	public void testConfiguration()
	{
		testConfigWrongFilePath();
		testConfigWrongEntries();
		testConfigEmptyFile();
	}
	
	private void testConfigWrongFilePath()
	{
		Configuration.loadFromFile("NoSuchFile.txt");
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
	
	private void testConfigWrongEntries()
	{
		Configuration.loadFromFile("./TestFiles/TestErrorConfig.txt");
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
	
	private void testConfigEmptyFile()
	{
		Configuration.loadFromFile("./TestFiles/TestEmptyConfig.txt");
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
	
	@Test(expected=Exception.class)
	public void testArchiver()
	{
		Archiver a = new Archiver();
		Configuration.loadFromFile("./TestFiles/testWrongLoginConfig.txt");
		assertTrue(a.needsDB());
		assertEquals(a.toString(),"Archiver");
		a.run();
	}

}
