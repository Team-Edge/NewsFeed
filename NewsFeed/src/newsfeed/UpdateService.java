package newsfeed;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import database.DBconnection;
import database.OrderInsertCustomFeedEntry;
import database.OrderInsertSourceFeedEntry;
import database.QueryFilterURLs;
import database.QueryFilterWords;
import database.QueryFilters;
import datatypes.SourceFeedEntry;
import feedanalyser.TextSearch;

public class UpdateService extends Thread {
	private DBconnection database;
	private int sourceFeedID;
	private List<SourceFeedEntry> newEntries;

	
	public UpdateService(DBconnection database, int sourceFeedID) {
		super();
		this.sourceFeedID = sourceFeedID;
		this.database = database;
		this.newEntries = new LinkedList<SourceFeedEntry>();
	}
	
	public synchronized void addEntry(SourceFeedEntry newEntry) {
		this.newEntries.add(newEntry);
		synchronized(this.newEntries) {
			this.newEntries.notify();
		}
	}
	
	private synchronized SourceFeedEntry getEntry() {
		if(this.newEntries.isEmpty()) {
			return null;
		}
		else {
			SourceFeedEntry next = this.newEntries.get(0);
			this.newEntries.remove(0);
			return next;
		}
	}

	@Override
	public void run() {
		SourceFeedEntry current =null;
		do {
			// get/wait for the next entry
			do {
				synchronized(this) {
					//thread tries to get the next entry from the queue
					current = this.getEntry();
				}
				if(current==null) {
					try {
						//thread waits until new entries are added to the queue
						//notify will automatically be called on adding a new entry
						synchronized(newEntries) {
							this.newEntries.wait();
						}
					} catch (InterruptedException e) {
						//thread ends on interrupt
						//interrupt is expected to indicate that there will be no more entries
						return;
					}
				}
			} while(current == null);
			
			//process the current entry
			try {
				processEntry(current);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				synchronized(current) {
					current.notifyAll();
				}
			}
			
		} while(true);	//no abort condition here; this can only be aborted by interrupt
	}
	
	
	private void processEntry(SourceFeedEntry newEntry) throws SQLException, Exception {
		//insert the entry to the database and get its ID
		int currentSourceFeedID = new OrderInsertSourceFeedEntry(this.database, newEntry, this.sourceFeedID).execute();
		//for each CustomFeed: check if the new entry matches the filters 
		try (TextSearch searcher = new TextSearch(newEntry)) {				
			List<Integer> filterIDs = new QueryFilters(this.database).getIDs();
			for(int currentFilter : filterIDs) {
				List<Integer> allowedSources = new QueryFilterURLs(this.database, currentFilter).getSourceFeedIDs();
				if(allowedSources.contains(Integer.valueOf(this.sourceFeedID))) {
					QueryFilterWords filterQuery = new QueryFilterWords(this.database, currentFilter);
					filterQuery.query();
					List<String> titleWords = filterQuery.getTitleWords();
					List<String> descrWords = filterQuery.getDescriptionWords();
					List<String> textWords = filterQuery.getTextWords();
					if(searcher.query(titleWords, descrWords, textWords) == true) {
						new OrderInsertCustomFeedEntry(this.database, currentSourceFeedID, currentFilter).execute();
					}
				}
			}		
		}	
	}

}
