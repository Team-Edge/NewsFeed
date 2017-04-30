package sourceFeedChecker;

import datatypes.SourceFeedFactory;
import program.IApplicationJob;

/**
 * job for archiving obsolete SourceFeedEntries
 */
public class SourceFeedChecker implements IApplicationJob {
	private String url;

	/**
	 * standard constructor
	 * @param url 	the url that has to be checked
	 */
	public SourceFeedChecker(String url) {
		this.url = url;
	}

	/**
	 * checks validity of the URL given as program argument
	 * program exits with 0 if SourceFeedEntries can be retrieved from the URL
	 * program exits with 1 otherwise
	 */
	@Override
	public void run() {
		if(SourceFeedFactory.testURL(this.url)) {
			System.out.println("SourceFeed URL ok");
			System.exit(0);
		} else {
			System.out.println("Failed to extract feeds from this URL");
			System.exit(1);
		}
	}

	@Override
	public boolean needsDB() {
		return false;
	}

	@Override
	public String toString() {
		return "SourceFeedChecker";
	}
	
}

