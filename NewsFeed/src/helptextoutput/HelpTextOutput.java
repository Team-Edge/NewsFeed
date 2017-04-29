package helptextoutput;

import program.IApplicationJob;

/**
 * job for printing an application description to stdout
 */
public class HelpTextOutput implements IApplicationJob {

	/**
	 * standard constructor
	 */
	public HelpTextOutput() {
	}

	/**
	 * prints an application description to stdout
	 */
	@Override
	public void run() {
		System.out.println("Use program arguments to determine tasks of the program. ");
		System.out.println("The application will execute the tasks in the order of their arguments. ");
		System.out.println();
		System.out.println("-?");
		System.out.println("--help");
		System.out.println("Output this helptext");
		System.out.println();
		System.out.println("-c filepath");
		System.out.println("--config filepath");
		System.out.println("read configuration from filepath. Standard configuration is read from ./config.txt");
		System.out.println();
		System.out.println("newscrawler");
		System.out.println("Newscrawler");
		System.out.println("NewsCrawler");
		System.out.println("Run newscrawler service and search for new entries");
		System.out.println();
		System.out.println("archiver");
		System.out.println("Archiver");
		System.out.println("Move old SourceFeedEntries to archive (for performance reasons)");
		System.out.println();
		System.out.println("filterupdate [filterID]");
		System.out.println("Filterupdate [filterID]");
		System.out.println("FilterUpdate [filterID]");
		System.out.println("Call this after creating or updating a profile filter. Detects matches again for existing entries. ");
		System.out.println();
		System.out.println("checksourcefeed [url]");
		System.out.println("Checksourcefeed [url]");
		System.out.println("CheckSourcefeed [url]");
		System.out.println("SheckSourceFeed [url]");
		System.out.println("checks if the url is a valid feed url. ");
		System.out.println("program exits after executing this with code 0 if url is valid. ");
		System.out.println("program exits after executing this with code 1 otherwise. ");
		System.out.println();
	}

	@Override
	public boolean needsDB() {
		return false;
	}
	
	@Override
	public String toString() {
		return "HelpTextOutput";
	}

}
