package newsfeed;

import java.util.List;

import datatypes.SourceFeedEntry;


public class EnlargingService extends Thread {
	private List<SourceFeedEntry> toEnlarge;
	
	public EnlargingService(List<SourceFeedEntry> toEnlarge) {
		this.toEnlarge = toEnlarge;
	}

	@Override
	public void run() {
		for(SourceFeedEntry current : this.toEnlarge) {
			current.enlarge();
			current.notify();
		}		
	}

}
