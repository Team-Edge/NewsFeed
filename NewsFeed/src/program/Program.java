package program;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


/**
 * static Program class containing the main() method
 */
public class Program {

	/**
	 * The application's start point
	 * @param args Program arguments
	 */
	public static void main(String[] args) {
		Configuration.loadFromFile("./config.txt");
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
        if(Configuration.getGeneralBeVerbose()) {
        	System.out.println(sdf.format(cal.getTime()) + " : Program started");
        }
		try {
			List<IApplicationJob> todo = new LinkedList<IApplicationJob>();
			try {
				todo = Program.parseArgs(args);
			} catch (Exception e) {
				cal = Calendar.getInstance();
				System.err.println(sdf.format(cal.getTime()) + " : Argument parsing failed");
				System.err.println(e.getMessage());
				System.err.println();
			}
			for(IApplicationJob currentJob : todo) {
				if(Configuration.getGeneralBeVerbose()) {
			        System.out.println(sdf.format(cal.getTime()) + " : Switching to next job: " + currentJob.toString());
			    }
				try {
					currentJob.run();
				} catch (Exception e) {
					cal = Calendar.getInstance();
					System.err.println(sdf.format(cal.getTime()) + " : Job Execution failed");
					System.err.println(e.getMessage());
					System.err.println();
				}
			}
		} catch (Throwable t) {
			cal = Calendar.getInstance();
			System.err.println(sdf.format(cal.getTime()) + " : Unknown error in Program.main(). Program aborted. ");
			System.err.println(t.getMessage());
			System.err.println();
		} finally {
			if(Configuration.getGeneralBeVerbose()) {
				cal = Calendar.getInstance();
				System.out.println(sdf.format(cal.getTime()) + " : Program stopped");
			}
		}
	}
	
	/**
	 * generates a list containing jobs to be executed by the program
	 * @param args program arguments to be parsed to 
	 * @return list of jobs the program has to perform
	 * @throws Exception if the argument list is malformed
	 * @see IApplicationJob
	 */
	private static List<IApplicationJob> parseArgs(String[] args) throws Exception
	{
		List<IApplicationJob> ret = new LinkedList<IApplicationJob>();
		if(args.length == 0) {
			throw new Exception("No arguments found");
		}
		for(int i = 0; i<args.length; i++) {
			switch(args[i]) {
			case "-?":				//help
			case "--help":
				ret.add(new HelpTextOutput());
				break;
			case "-c":				//config
			case "--config":
				i++;
				if(i >= args.length) {
					throw new Exception("Missing file path argument for option -c");
				}
				if(new File(args[i]).isFile()) {
					ret.add(new ConfigLoad(args[i]));
				} else {
					throw new Exception("Invalid config filepath: " + args[i]);
				}
				break;
			case "newscrawler":		//newscrawler
			case "Newscrawler":
			case "NewsCrawler":
				ret.add(new NewsCrawler());
				break;
			case "archiver":		//archiver
			case "Archiver":
				ret.add(new Archiver());
				break;
				
			default: throw new Exception("Invalid Argument: " + args[i]);
			}		
		}
		return ret;
	}
	

}
