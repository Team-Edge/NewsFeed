package feedanalyser;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
		Scanner feedIn = new Scanner(feedAddress.openStream());
		List<String> feedLines = new ArrayList<String>();
		while((line = feedIn.nextLine()) != null )
			feedLines.add(line);
		feedIn.close();
		
		this.patch = DiffUtils.diff(cacheLines, feedLines);
		
		for(Delta delta : this.patch.getDeltas())
		{
			hasDeltas = true;
			TYPE type = delta.getType();
			if(type == TYPE.INSERT) this.hasInsertDeltas = true;
			if(type == TYPE.DELETE) this.hasDeleteDeltas = true;
		}
	}
	
	public void applyToCache() throws Exception
	{
		String line;
		File cacheFile = new File(this.cacheFilePath);
		BufferedReader cacheIn = new BufferedReader(new FileReader(cacheFile));
		List<String> cacheLines = new ArrayList<String>();
		while((line = cacheIn.readLine()) != null )
			cacheLines.add(line);
		cacheIn.close();
		
		this.patch.applyTo(cacheLines);

		BufferedWriter cacheOut = new BufferedWriter(new FileWriter(cacheFile));
		for(String toWrite : cacheLines)
		{
			cacheOut.write(toWrite);
			cacheOut.newLine();
		}
		cacheOut.close();	
	}
	
	public boolean hasInsertions() {
		return hasInsertDeltas;
	}

	public boolean hasDeletions() {
		return hasDeleteDeltas;
	}

	public boolean hasChanges() {
		return hasDeltas;
	}
		
}
