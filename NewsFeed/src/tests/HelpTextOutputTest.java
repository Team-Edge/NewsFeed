package tests;

import static org.junit.Assert.*;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import helptextoutput.*;


public class HelpTextOutputTest {
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    @Before
    public void setup()
    {
    	System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
	
	@Test
	public void testHelpTextOutput()
	{
		HelpTextOutput h = new HelpTextOutput();
		assertFalse(h.needsDB());
		assertEquals(h.toString(),"HelpTextOutput");
		h.run();
		assertTrue(outContent.toString().contains("-?"));
		assertTrue(outContent.toString().contains("--help"));
		assertTrue(outContent.toString().contains("-c"));
		assertTrue(outContent.toString().contains("--config"));
		assertTrue(outContent.toString().contains("newscrawler"));
		assertTrue(outContent.toString().contains("archiver"));
		assertTrue(outContent.toString().contains("filterupdate"));
	}
	
	@After
    public void cleanUpStreams() {
        System.setOut(null);
        System.setErr(null);
    }

}
