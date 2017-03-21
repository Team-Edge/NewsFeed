package dataTypes;

public class SourceFeed {
	private int id;
	private String url;
	private String cache;
	
	public SourceFeed(int id, String url, String cache) {
		this.id = id;
		this.url = url;
		this.cache = cache;
	}

	public int getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getCache() {
		return cache;
	}

}
