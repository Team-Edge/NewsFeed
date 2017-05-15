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
	
	@Test
	public void testArchiver()
	{
		Archiver a = new Archiver();
		assertTrue(a.needsDB());
		assertEquals(a.toString(),"Archiver");
	}
	
	@After
	public void cleanUp()
	{
		Configuration.loadFromFile("./config.txt");
	}

}
