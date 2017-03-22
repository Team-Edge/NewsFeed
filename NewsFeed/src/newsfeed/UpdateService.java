package newsfeed;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import database.DBconnection;
import database.OrderInsertCustomFeedEntry;
import database.OrderInsertSourceFeedEntry;
import database.QueryCustomFeedIDs;
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
		this.sourceFeedID = sourceFeedID;
		this.database = database;
		this.newEntries = new LinkedList<SourceFeedEntry>();
	}
	
	public synchronized void addEntry(SourceFeedEntry newEntry) {
		this.newEntries.add(newEntry);
		this.newEntries.notify();
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
				synchronized(this)
				{
					//thread tries to get the next entry from the queue
					current = this.getEntry();
				}
				if(current==null) {
					try {
						//thread waits until new entries are added to the queue
						//notify will automatically be called on adding a new entry
						this.newEntries.wait();
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
			}
			
		} while(true);	//no abort condition here; this can only be aborted by interrupt
	}
	
	
	private void processEntry(SourceFeedEntry newEntry) throws SQLException, Exception {
		//insert the entry to the database and get its ID
		int currentSourceFeedID = new OrderInsertSourceFeedEntry(this.database, newEntry, this.sourceFeedID).execute();
		//for each CustomFeed: check if the new entry matches the filters 
		TextSearch searcher = new TextSearch(newEntry);		
		List<Integer> profileIDs = new QueryCustomFeedIDs(this.database).getIDs();		
		for(int currentProfile : profileIDs) {			
			List<Integer> filterIDs = new QueryFilters(this.database, currentProfile).getIDs();
			boolean filterPassed = false;
			for(int currentFilter : filterIDs) {
				List<Integer> allowedSources = new QueryFilterURLs(this.database, currentFilter).getSourceFeedIDs();
				if(allowedSources.contains(Integer.valueOf(this.sourceFeedID))) {
					QueryFilterWords filterQuery = new QueryFilterWords(this.database, currentFilter);
					filterQuery.query();
					List<String> titleWords = filterQuery.getTitleWords();
					List<String> descrWords = filterQuery.getDescriptionWords();
					List<String> textWords = filterQuery.getTextWords();
					//here is the actual filtering
					filterPassed = searcher.query(titleWords, descrWords, textWords);
					if(filterPassed == true) {
						//it's enough if there is one matching filter in a profile
						break;
					}
				}
			}
			// add the Entry to the CustomFeed if it matches its filters
			if(filterPassed == true) {
				new OrderInsertCustomFeedEntry(this.database, currentSourceFeedID, currentProfile).execute();
			}			
		}
		searcher.close();		
	}

}
