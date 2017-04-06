package tests;

import static org.junit.Assert.*;
import org.junit.Test;

import helptextoutput.*;

public class HelpTextOutputTest {

	@Test
	public void testHelpTextOutput()
	{
		HelpTextOutput h = new HelpTextOutput();
		assertFalse(h.needsDB());
		assertEquals(h.toString(),"HelpTextOutput");
	}

}
