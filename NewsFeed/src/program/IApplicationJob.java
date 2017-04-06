package program;

/**
 * interface for jobs that the program has to perform
 */
public interface IApplicationJob extends Runnable {

	/**
	 * indicates if the job needs a connection to the database while running
	 * @return true if the job needs a connection to the database while running
	 */
	public boolean needsDB();
	
	/**
	 * returns a String containing the name of the task
	 * @return a String containing the name of the task
	 */
	@Override
	public String toString();
	
}
