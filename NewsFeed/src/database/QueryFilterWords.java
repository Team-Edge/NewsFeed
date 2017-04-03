package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * SQL query to get all Words to a given filter ID
 */
public class QueryFilterWords {
	private SqlQuery wrapped;
	private List<String> titleWords;
	private List<String> descrWords;
	private List<String> textWords;
	private List<String> invTitleWords;
	private List<String> invDescrWords;
	private List<String> invTextWords;
	
	/**
	 * standard constructor
	 * 
	 * @param database		DBconnection for the server to which the query will be sent
	 * @param filterID		ID of the filter for that URLs are filtered
	 * @throws SQLException	if the connection fails or parameters are malformed
	 * @see DBconnection
	 */
	public QueryFilterWords(DBconnection database, int filterID) throws SQLException {
		this.titleWords = new LinkedList<String>();
		this.descrWords = new LinkedList<String>();
		this.textWords = new LinkedList<String>();
		this.invTitleWords = new LinkedList<String>();
		this.invDescrWords = new LinkedList<String>();
		this.invTextWords = new LinkedList<String>();
		this.wrapped = new SqlQuery(database, "SELECT Keyword, SearchIndex, Inverted FROM Newsfeed.FilterKeyword WHERE Filter_ID = "+filterID+";");
	}

	/**
	 * sends the query to the server for execution
	 * 
	 * @throws Exception if connection or execution fails
	 */
	public void query() throws Exception {
		ResultSet result = null;
		try {
			this.titleWords.clear();
			this.descrWords.clear();
			this.textWords.clear();
			this.invTitleWords.clear();
			this.invDescrWords.clear();
			this.invTextWords.clear();
			this.wrapped.query();
			result = this.wrapped.getResult();
			while(result.next())
			{
				int searchIndex = result.getInt(2);
				boolean inverted = result.getBoolean(3);
				switch(searchIndex)
				{
				case 1: if(inverted) {
							invDescrWords.add(result.getString(1));
						} else {
							descrWords.add(result.getString(1));
						}
						break;
				case 2: if(inverted) {
							invTextWords.add(result.getString(1));
						} else {
							textWords.add(result.getString(1));
						}
						break;
				default: if(inverted) {
							invTitleWords.add(result.getString(1));
						} else {
							titleWords.add(result.getString(1));
						}
						break;
				}
			}
		} finally {
			if(result!=null) {
				result.close();
			}
			wrapped.close();
		}
	}

	/**
	 * returns all words for search in the title from the query result
	 * @return all words for search in the title from the query result
	 */
	public List<String> getTitleWords() {
		return titleWords;
	}

	/**
	 * returns all words for search in the description from the query result
	 * @return all words for search in the description from the query result
	 */
	public List<String> getDescriptionWords() {
		return descrWords;
	}

	/**
	 * returns all words for search in the full text from the query result
	 * @return all words for search in the full text from the query result
	 */
	public List<String> getTextWords() {
		return textWords;
	}

	/**
	 * returns all words that are not allowed in titles from the query result
	 * @return all words that are not allowed in titles from the query result
	 */
	public List<String> getInvTitleWords() {
		return invTitleWords;
	}

	/**
	 * returns all words that are not allowed in descriptions from the query result
	 * @return all words that are not allowed in descriptions from the query result
	 */
	public List<String> getInvDescrWords() {
		return invDescrWords;
	}

	/**
	 * returns all words that are not allowed in full texts from the query result
	 * @return all words that are not allowed in full texts from the query result
	 */
	public List<String> getInvTextWords() {
		return invTextWords;
	}
		
}

