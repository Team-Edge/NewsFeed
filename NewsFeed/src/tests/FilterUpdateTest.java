package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import filterUpdate.*;
import program.Configuration;

public class FilterUpdateTest {

	@Test(expected=Exception.class)
	public void testFilterUpdate()
	{
		FilterUpdate f = new FilterUpdate(-1);
		Configuration.loadFromFile("./TestFiles/testWrongLoginConfig.txt");
		assertTrue(f.needsDB());
		assertEquals(f.toString(),"FilterUpdate [filterID=-1]");
		f.run();
	}
}
