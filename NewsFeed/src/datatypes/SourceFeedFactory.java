package datatypes;

public class SourceFeedFactory {

	public static SourceFeed createSourceFeed(int id, String url, String cachefile) {
		if(url.contains("www.facebook.com")) {
			return new SourceFeedFBImpl(id, url, cachefile);
		} else {
			return new SourceFeedRSSImpl(id, url, cachefile);
		}
	}
	
	public static boolean testURL(String url) {
		if(url.contains("www.facebook.com")) {
			return SourceFeedFBImpl.testURL(url);
		} else {
			return SourceFeedRSSImpl.testURL(url);
		}
	}

}
