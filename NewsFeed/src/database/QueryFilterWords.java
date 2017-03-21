package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;


public class QueryFilterWords {
	private SqlQuery wrapped;
	private List<String> titleWords;
	private List<String> descrWords;
	private List<String> textWords;
	
	public QueryFilterWords(DBconnection database, int filterID) throws SQLException {
		this.titleWords = new LinkedList<String>();
		this.descrWords = new LinkedList<String>();
		this.textWords = new LinkedList<String>();
		this.wrapped = new SqlQuery(database, "SELECT Keyword, SearchIndex FROM Newsfeed.FilterKeyword WHERE Filter_ID = "+filterID+";");
	}

	public void query() throws Exception {
		this.titleWords.clear();
		this.descrWords.clear();
		this.textWords.clear();
		this.wrapped.query();
		ResultSet result= this.wrapped.getResult();
		while(result.next())
		{
			int searchIndex = result.getInt(2);
			switch(searchIndex)
			{
			case 1: descrWords.add(result.getString(1)); break;
			case 2: textWords.add(result.getString(1)); break;
			default: titleWords.add(result.getString(1)); break;
			}
		}
		this.wrapped.close();
	}

	public List<String> getTitleWords() {
		return titleWords;
	}

	public List<String> getDescriptionWords() {
		return descrWords;
	}

	public List<String> getTextWords() {
		return textWords;
	}
		
}

