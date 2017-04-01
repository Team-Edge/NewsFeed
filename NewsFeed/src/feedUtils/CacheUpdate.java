package feedUtils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import difflib.Patch;
import difflib.Delta.TYPE;
import program.Configuration;
import difflib.Delta;
import difflib.DiffUtils;

/**
 * Class for fetching a file from an URL, comparing it with a local cache and and updating the cache
 */
public class CacheUpdate {
	private String feedUrl;
	private String cacheFilePath;
	private Patch patch;
	private boolean hasDeltas;
	private boolean hasInsertDeltas;
	private boolean hasDeleteDeltas;
	
	/**
	 * standard constructor
	 * 
	 * @param feedUrl		URL of the new version
	 * @param cacheFilePath	Path of the cache file
	 * @throws Exception	on I/O error while reading from file or URL
	 */
	public CacheUpdate(String feedUrl, String cacheFilePath) throws Exception {
		this.feedUrl = feedUrl;
		this.cacheFilePath = cacheFilePath;
		this.patch = null;
		this.hasInsertDeltas = false;
		this.hasDeleteDeltas = false;
		this.hasDeltas = false;
		this.diff();
	}

	/**
	 * reads and calculates the differences between old and new version
	 * automatically called by constructor
	 * 
	 * @throws Exception on I/O error while fetching
	 */
	private void diff() throws Exception {
		String line;
		
		File cacheFile = new File(this.cacheFilePath);
		cacheFile.createNewFile();	//This does nothing if file already exists
		BufferedReader cacheIn = new BufferedReader(new FileReader(cacheFile));
		List<String> cacheLines = new ArrayList<String>();
		while((line = cacheIn.readLine()) != null ) {
			cacheLines.add(line);
		}
		cacheIn.close();
		
		URL feedAddress = new URL(feedUrl);
		URLConnection feedCon = feedAddress.openConnection();
		feedCon.setConnectTimeout(Configuration.getNetworkConnectionTimeout());
		feedCon.setReadTimeout(Configuration.getNetworkReadTimeout());
		BufferedReader feedIn = new BufferedReader(new InputStreamReader(feedCon.getInputStream()));
		List<String> feedLines = new ArrayList<String>();
		while((line = feedIn.readLine()) != null ) {
			feedLines.add(line);
		}
		feedIn.close();
		
		this.patch = DiffUtils.diff(cacheLines, feedLines);
		
		for(Delta delta : this.patch.getDeltas()) {
			this.hasDeltas = true;
			TYPE type = delta.getType();
			if(type == TYPE.INSERT) {
				this.hasInsertDeltas = true;
			}
			if(type == TYPE.DELETE) { 
				this.hasDeleteDeltas = true;
			}
		}
	}
	
	
	/**
	 * updates the local cache to the state of the version from the URL
	 * @throws Exception if the URL could not be found
	 */
	@SuppressWarnings("unchecked")
	public void applyToCache() throws Exception {
		String line;
		File cacheFile = new File(this.cacheFilePath);
		BufferedReader cacheIn = new BufferedReader(new FileReader(cacheFile));
		List<String> cacheLines = new ArrayList<String>();
		while((line = cacheIn.readLine()) != null ) {
			cacheLines.add(line);
		}
		cacheIn.close();
		
		cacheLines = (List<String>)this.patch.applyTo(cacheLines);

		BufferedWriter cacheOut = new BufferedWriter(new FileWriter(cacheFile));
		for(String toWrite : cacheLines) {
			cacheOut.write(toWrite);
			cacheOut.newLine();
		}
		cacheOut.close();	
	}
	
	/**
	 * indicates if there are new insertions in the current version
	 * @return true if there are new insertions in the current version
	 */
	public boolean hasInsertions() {
		return this.hasInsertDeltas;
	}

	/**
	 * indicates if there are deletions from the old version
	 * @return true if there are deletions from the old version
	 */
	public boolean hasDeletions() {
		return this.hasDeleteDeltas;
	}

	/**
	 * indicates if there are any differences between both versions
	 * @return true if there are differences between both versions
	 */
	public boolean hasChanges() {
		return this.hasDeltas;
	}
		
}
