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
	 */
	public SourceFeedChecker(String url) {
		this.url = url;
	}

	/**
	 * moves old SourceFeedEntries to archive table
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

