package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseTest.class, DatatypesTest.class, FeedUtilsTest.class, NewsCrawlerTest.class,
		ProgramTest.class })
public class AllTests {

}
