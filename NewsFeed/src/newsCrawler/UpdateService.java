package newsCrawler;

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
import feedUtils.TextSearch;

/**
 * Thread to process SourceFeedEntries
 * @see SourceFeedEntry
 */
public class UpdateService extends Thread {
	private DBconnection database;
	private int sourceFeedID;
	private List<SourceFeedEntry> newEntries;

	/**
	 * standard constructor
	 * @param database DBconnection of the database where the new SourceFeedEntries are written to
	 * @param sourceFeedID ID of the SourceFeed where the new entries will belong to
	 */
	public UpdateService(DBconnection database, int sourceFeedID) {
		super();
		this.sourceFeedID = sourceFeedID;
		this.database = database;
		this.newEntries = new LinkedList<SourceFeedEntry>();
	}
	
	/**
	 * adds a SourceFeedEntry to the queue of entries that have to be processed
	 * @param newEntry entry that will be processed and notified
	 */
	public synchronized void addEntry(SourceFeedEntry newEntry) {
		this.newEntries.add(newEntry);
		synchronized(this.newEntries) {
			this.newEntries.notify();
		}
	}
	
	/**
	 * fetches the next entry from the queue of entries that have to be processed
	 * @return the next entry of the queue or null if there is no next one
	 */
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

	/**
	 * loop for fetching, processing and notifying SourceFeedEntries from the queue
	 */
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
	
	/**
	 * adds a new entry to the database, tests it for fitting to all filters and adds matches to DB
	 * @param newEntry		the entry to be processed
	 * @throws SQLException	if an SQL query fails
	 * @throws Exception	if another error occurs
	 */
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
					List<String> invTitleWords = filterQuery.getInvTitleWords();
					List<String> invDescrWords = filterQuery.getInvDescrWords();
					List<String> invTextWords = filterQuery.getInvTextWords();
					if(searcher.query(titleWords, descrWords, textWords, invTitleWords, invDescrWords, invTextWords) == true) {
						new OrderInsertCustomFeedEntry(this.database, currentSourceFeedID, currentFilter).execute();
					}
				}
			}		
		}	
	}

}
