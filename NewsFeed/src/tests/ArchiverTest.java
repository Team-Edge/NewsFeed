package tests;

import static org.junit.Assert.*;
import org.junit.*;

import archiver.*;
import program.Configuration;

public class ArchiverTest {

	@Before
	public void setup()
	{
		Configuration.loadFromFile("./TestFiles/testWrongLoginConfig.txt");
	}
	
	@Test(expected=Exception.class)
	public void testArchiver()
	{
		Archiver a = new Archiver();
		assertTrue(a.needsDB());
		assertEquals(a.toString(),"Archiver");
		a.run();
	}
	
	@After
	public void cleanUp()
	{
		Configuration.loadFromFile("./config.txt");
	}

}
