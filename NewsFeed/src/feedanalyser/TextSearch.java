package feedanalyser;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import datatypes.SourceFeedEntry;



public class TextSearch implements Closeable {
	private Analyzer analyzer;
	private Directory index;


	public TextSearch(SourceFeedEntry article) throws IOException {
		this.analyzer = new StandardAnalyzer();
		this.index = new RAMDirectory();
		new IndexWriterConfig(analyzer);
		IndexWriter w = new IndexWriter(index, new IndexWriterConfig(analyzer));
		Document doc = new Document();
		doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
		doc.add(new TextField("description", article.getDescription(), Field.Store.YES));
		doc.add(new TextField("text", article.getText(), Field.Store.YES));
		w.addDocument(doc);
		w.close();
	}
	
	public boolean query(List<String> titleWords, List<String> descrWords, List<String> textWords) throws ParseException, IOException
	{
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		for(String word : titleWords)
		{
			Query subquery = new QueryParser("title", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.FILTER);
		}
		for(String word : descrWords)
		{
			Query subquery = new QueryParser("description", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.FILTER);
		}
		for(String word : textWords)
		{
			Query subquery = new QueryParser("text", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.FILTER);
		}
		Query query = queryBuilder.build();
		
	    IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(1);
	    searcher.search(query, collector);
	    int numOfHits = collector.getTotalHits();
	    reader.close();
	    
	    if(numOfHits > 0) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	

	public static void main(String[] args)
	{
		try
		{
			//	Specify the analyzer for tokenizing text.
		    //	The same analyzer should be used for indexing and searching
			StandardAnalyzer analyzer = new StandardAnalyzer();
			
			//	Code to create the index
			Directory index = new RAMDirectory();
			
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			
			IndexWriter w = new IndexWriter(index, config);
			addDoc(w, "Lucene in Action", "193398817");
			addDoc(w, "Lucene for Dummies", "55320055Z");
			addDoc(w, "Managing Gigabytes", "55063554A");
			addDoc(w, "The Art of Computer Science", "9900333X");
			addDoc(w, "My name is teja", "12842d99");
			addDoc(w, "Lucene demo by teja", "23k43413");
			w.close();
			
			//	Text to search
			String querystr = args.length > 0 ? args[0] : "teja";
			
			//	The "title" arg specifies the default field to use when no field is explicitly specified in the query
			Query q = new QueryParser("title", analyzer).parse(querystr);
			
			// Searching code
			int hitsPerPage = 10;
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    //	Code to display the results of search
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) 
		    {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
		    }
		    
		    // reader can only be closed when there is no need to access the documents any more
		    reader.close();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	private static void addDoc(IndexWriter w, String title, String isbn) throws IOException 
	{
		  Document doc = new Document();
		  // A text field will be tokenized
		  doc.add(new TextField("title", title, Field.Store.YES));
		  // We use a string field for isbn because we don\'t want it tokenized
		  doc.add(new StringField("isbn", isbn, Field.Store.YES));
		  w.addDocument(doc);
	}



	@Override
	public void close() throws IOException {
		this.index.close();
		this.analyzer.close();	
	}
	
}
