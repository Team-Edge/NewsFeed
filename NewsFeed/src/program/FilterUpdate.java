package program;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import database.DBconnection;
import database.OrderInsertCustomFeedEntry;
import database.QueryEntriesToSource;
import database.QueryFilterURLs;
import database.QueryFilterWords;
import datatypes.SourceFeedEntry;
import feedUtils.TextSearch;

/**
 *
 */
public class FilterUpdate implements IApplicationJob {
	private int filterID;

	/**
	 * standard constructor
	 */
	public FilterUpdate(int id) {
		this.filterID = id;
	}

	/**
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Configuration.getGeneralOutputDateFormat());
		DBconnection database = null;		
		try {
			database = new DBconnection("jdbc:mysql://" + Configuration.getDbServerHostName()
										+ ":" + Configuration.getDbServerPort()
										+ "/" + Configuration.getDbServerSchemaName(), 
										Configuration.getDbServerUserName(), 
										Configuration.getDbServerPassword());
			try {
				
				List<Integer> feedIDs = new QueryFilterURLs(database, filterID).getSourceFeedIDs();
				QueryFilterWords qry = new QueryFilterWords(database, filterID);
				qry.query();
				for(int currentFeed : feedIDs) {
					List<SourceFeedEntry> entries = new QueryEntriesToSource(database, currentFeed).getEntries();
					for(SourceFeedEntry currentEntry : entries) {
						boolean hit = false;
						try (TextSearch searcher = new TextSearch(currentEntry);) {
							hit = searcher.query(qry.getTitleWords(), qry.getDescriptionWords(), qry.getTextWords(), 
										qry.getInvTitleWords(), qry.getInvDescrWords(), qry.getInvTextWords());
							if(hit) {
								new OrderInsertCustomFeedEntry(database, currentEntry.getID(), this.filterID).execute();
							}
						} catch (IOException e) {
							cal = Calendar.getInstance();
							System.err.println(sdf.format(cal.getTime()) + " : Executing FullTextSearch failed");
							System.err.println(e.getMessage());
							System.err.println();
							continue;
						} catch (SQLException e) {
							cal = Calendar.getInstance();
							System.err.println(sdf.format(cal.getTime()) + " : Connecting FeedEntry to profile failed");
							System.err.println(e.getMessage());
							System.err.println();
						} catch (Exception e) {
							cal = Calendar.getInstance();
							System.err.println(sdf.format(cal.getTime()) + " : Unexpected Error while comparing new filter with old entries");
							System.err.println(e.getMessage());
							System.err.println();
						}
					}
				}
					
			} catch (Exception e) {
				cal = Calendar.getInstance();
				System.err.println(sdf.format(cal.getTime()) + " : Executing query failed");
				System.err.println(e.getMessage());
				System.err.println();
			}
		} catch (SQLException e) {
			cal = Calendar.getInstance();
			System.err.println(sdf.format(cal.getTime()) + " : DB-Login failed");
			System.err.println(e.getMessage());
			System.err.println();
		} catch (Exception e) {
			cal = Calendar.getInstance();
			System.err.println(sdf.format(cal.getTime()) + " : Unknown error while executing NewsCrawler");
			System.err.println(e.getMessage());
			System.err.println();
		} finally {
			try {
				database.close();
			} catch (IOException e) {
				// ignore
				// program has ended and DB-Server will disconnect automatically if we could not. 
			}
		}

	}

	/**
	 * @see program.IApplicationJob#needsDB()
	 */
	@Override
	public boolean needsDB() {
		return true;
	}

}
