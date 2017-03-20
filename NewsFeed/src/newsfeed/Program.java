package newsfeed;

import java.util.LinkedList;
import java.util.List;

import feedanalyser.TextSearch;

public class Program {

	public static void main(String[] args)
	{
		NewsCrawlerUpdate upd = new NewsCrawlerUpdate("http://www.tagesschau.de/xml/rss2", "C:/Users/Florian/Documents/cacheTagesschau.txt");
		upd.doWork();
		System.out.println("---");
		for(feedanalyser.Feed i : upd.getOldEntries())
			System.out.println("old " + i);
		System.out.println("---");
		for(feedanalyser.Feed i : upd.getNewEntries())
		{
			i.enlarge();
			System.out.println("new " + i);
		}
		System.out.println("---");
		
		for(feedanalyser.Feed i : upd.getNewEntries())
		{
			List<String> keyWordsColTrump = new LinkedList<String>();
			keyWordsColTrump.add("Trump");
			List<String> keyWordsColSchulz = new LinkedList<String>();
			keyWordsColSchulz.add("Schulz");
			keyWordsColSchulz.add("SPD");
			try {
				TextSearch searcher = new TextSearch(i);
				if(searcher.query(keyWordsColTrump, 1))
					System.out.println("Contains Trump: " + i);
				if(searcher.query(keyWordsColSchulz, 1))
					System.out.println("Contains Schulz and SPD: " + i);
				searcher.close();
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}

		}
		System.out.println("---");
		
	}

}
