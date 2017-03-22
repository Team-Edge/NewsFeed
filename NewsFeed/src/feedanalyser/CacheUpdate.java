package feedanalyser;


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
import difflib.Delta;
import difflib.DiffUtils;


public class CacheUpdate {
	private String feedUrl;
	private String cacheFilePath;
	private Patch patch;
	private boolean hasDeltas;
	private boolean hasInsertDeltas;
	private boolean hasDeleteDeltas;
	
	
	public CacheUpdate(String feedUrl, String cacheFilePath) throws Exception {
		this.feedUrl = feedUrl;
		this.cacheFilePath = cacheFilePath;
		this.patch = null;
		this.hasInsertDeltas = false;
		this.hasDeleteDeltas = false;
		this.hasDeltas = false;
		this.diff();
	}

	private void diff() throws Exception
	{
		String line;
		
		File cacheFile = new File(this.cacheFilePath);
		cacheFile.createNewFile();	//This does nothing if file already exists
		BufferedReader cacheIn = new BufferedReader(new FileReader(cacheFile));
		List<String> cacheLines = new ArrayList<String>();
		while((line = cacheIn.readLine()) != null )
			cacheLines.add(line);
		cacheIn.close();
		
		URL feedAddress = new URL(feedUrl);
		URLConnection feedCon = feedAddress.openConnection();
		feedCon.setConnectTimeout(10000);
		feedCon.setReadTimeout(10000);
		BufferedReader feedIn = new BufferedReader(new InputStreamReader(feedCon.getInputStream()));
		List<String> feedLines = new ArrayList<String>();
		while((line = feedIn.readLine()) != null )
			feedLines.add(line);
		feedIn.close();
		
		this.patch = DiffUtils.diff(cacheLines, feedLines);
		
		for(Delta delta : this.patch.getDeltas())
		{
			this.hasDeltas = true;
			TYPE type = delta.getType();
			if(type == TYPE.INSERT) this.hasInsertDeltas = true;
			if(type == TYPE.DELETE) this.hasDeleteDeltas = true;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void applyToCache() throws Exception
	{
		String line;
		File cacheFile = new File(this.cacheFilePath);
		BufferedReader cacheIn = new BufferedReader(new FileReader(cacheFile));
		List<String> cacheLines = new ArrayList<String>();
		while((line = cacheIn.readLine()) != null )
			cacheLines.add(line);
		cacheIn.close();
		
		cacheLines = (List<String>)this.patch.applyTo(cacheLines);

		BufferedWriter cacheOut = new BufferedWriter(new FileWriter(cacheFile));
		for(String toWrite : cacheLines)
		{
			cacheOut.write(toWrite);
			cacheOut.newLine();
		}
		cacheOut.close();	
	}
	
	public boolean hasInsertions() {
		return this.hasInsertDeltas;
	}

	public boolean hasDeletions() {
		return this.hasDeleteDeltas;
	}

	public boolean hasChanges() {
		return this.hasDeltas;
	}
		
}
