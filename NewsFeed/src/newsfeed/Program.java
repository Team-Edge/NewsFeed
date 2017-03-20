package newsfeed;

public class Program {

	public static void main(String[] args)
	{
		NewsCrawlerUpdate upd = new NewsCrawlerUpdate("http://www.tagesschau.de/xml/rss2", "./cacheTagesschau.txt");
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
	}

}
