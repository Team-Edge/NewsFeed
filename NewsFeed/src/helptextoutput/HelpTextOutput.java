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
		System.out.println("Run newscrawler");
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
