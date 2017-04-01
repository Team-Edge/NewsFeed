package newsCrawler;

import java.util.List;

import datatypes.SourceFeedEntry;

/**
 * Thread to enlarge SourceFeedEntries
 * @see SourceFeedEntry
 * @see feedUtils.FeedEnlarger
 */
public class EnlargingService extends Thread {
	private List<SourceFeedEntry> toEnlarge;
	
	/**
	 * standard constructor 
	 * @param toEnlarge list of SourceFeedEntries which have to be enlarged
	 */
	public EnlargingService(List<SourceFeedEntry> toEnlarge) {
		super();
		this.toEnlarge = toEnlarge;
	}

	/**
	 * enlarges all SourceFeedEntries from the list and notifies them
	 */
	@Override
	public void run() {
		for(SourceFeedEntry current : this.toEnlarge) {
			current.enlarge();
			synchronized(current) {
				current.notify();
			}
		}		
	}

}
