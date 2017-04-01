package feedUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import datatypes.SourceFeedEntry;


/**
 * class for full-text-search in SourceFeedEntries
 */
public class TextSearch implements Closeable {
	private Analyzer analyzer;
	private Directory index;

	/**
	 * standard constructor
	 * @param article the SourceFeedEntry containing the title, description and text that will be searched
	 * @throws IOException if creation of the index fails
	 * @see SourceFeedEntry
	 */
	public TextSearch(SourceFeedEntry article) throws IOException {
		this.analyzer = new StandardAnalyzer();
		this.index = new RAMDirectory();
		new IndexWriterConfig(analyzer);
		try (IndexWriter w = new IndexWriter(index, new IndexWriterConfig(analyzer))) {
			Document doc = new Document();
			doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
			doc.add(new TextField("description", article.getDescription(), Field.Store.YES));
			doc.add(new TextField("text", article.getText(), Field.Store.YES));
			w.addDocument(doc);
		}
	}
	
	/**
	 * queries the title, description and text of a SourceFeedEntry for several words
	 * @param titleWords		words that must occur in title
	 * @param descrWords		words that must occur in description
	 * @param textWords			words that must occur in text
	 * @param invTitleWords		words that must not occur in title
	 * @param invDescrWords		words that must not occur in description
	 * @param invTextWords		words that must not occur in text
	 * @return	true, if all words were found
	 * @throws ParseException	if query could not be parsed
	 * @throws IOException		if access to the indexed texts failed
	 */
	public boolean query(List<String> titleWords, List<String> descrWords, List<String> textWords, 
			List<String> invTitleWords, List<String> invDescrWords, List<String> invTextWords) throws ParseException, IOException {
		if(titleWords.isEmpty() && descrWords.isEmpty() && textWords.isEmpty()) {
			return true;
		}
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		for(String word : titleWords) {
			Query subquery = new QueryParser("title", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.MUST);
		}
		for(String word : descrWords) {
			Query subquery = new QueryParser("description", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.MUST);
		}
		for(String word : textWords) {
			Query subquery = new QueryParser("text", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.MUST);
		}
		for(String word : invTitleWords) {
			Query subquery = new QueryParser("title", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.MUST_NOT);
		}
		for(String word : invDescrWords) {
			Query subquery = new QueryParser("description", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.MUST_NOT);
		}
		for(String word : invTextWords) {
			Query subquery = new QueryParser("text", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.MUST_NOT);
		}
		Query query = queryBuilder.build();
		
		int numOfHits=0;
	    try (IndexReader reader = DirectoryReader.open(index)) {
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(1);
		    searcher.search(query, collector);
		    numOfHits = collector.getTotalHits();
	    }
	    
	    if(numOfHits > 0) {
	    	return true;
	    } else {
	    	return false;
	    }
	}

	@Override
	public void close() throws IOException {
		this.index.close();
		this.analyzer.close();	
	}
	
}
