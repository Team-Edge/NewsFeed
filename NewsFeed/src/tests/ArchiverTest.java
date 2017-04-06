package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import archiver.*;
import program.Configuration;

public class ArchiverTest {

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
