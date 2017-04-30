package newsCrawler;

import java.util.Collection;
import java.util.List;

import datatypes.SourceFeedEntry;
import genericObserver.Observable;

/**
 * Thread to enlarge SourceFeedEntries
 * @see SourceFeedEntry
 * @see feedUtils.FeedEnlarger
 */
public class EnlargingService extends Observable<SourceFeedEntry> implements Runnable {
	private List<SourceFeedEntry> toEnlarge;
	
	/**
	 * standard constructor 
	 * @param toEnlarge list of SourceFeedEntries which have to be enlarged
	 * @param observers list of Observers that are notified after each enlarge process
	 */
	public EnlargingService(List<SourceFeedEntry> toEnlarge, Collection<Observer<? super SourceFeedEntry>> observers) {
		super(observers);
		this.toEnlarge = toEnlarge;
	}

	/**
	 * enlarges all SourceFeedEntries from the list and notifies observers
	 */
	@Override
	public void run() {
		for(SourceFeedEntry current : this.toEnlarge) {
			current.enlarge();
			super.setChanged();
			super.notifyObservers(current);
		}
		super.setChanged();
		super.notifyObservers(null);
	}

}
