package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ArchiverTest.class, ConfigloadTest.class, DatatypesTest.class, FeedUtilsTest.class, FilterUpdateTest.class,
	HelpTextOutputTest.class })
public class AllTests {

}
