package feedanalyser;

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



public class TextSearch implements Closeable {
	private Analyzer analyzer;
	private Directory index;


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
	
	public boolean query(List<String> titleWords, List<String> descrWords, List<String> textWords) throws ParseException, IOException {
		if(titleWords.isEmpty() && descrWords.isEmpty() && textWords.isEmpty()) {
			return true;
		}
		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		for(String word : titleWords) {
			Query subquery = new QueryParser("title", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.FILTER);
		}
		for(String word : descrWords) {
			Query subquery = new QueryParser("description", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.FILTER);
		}
		for(String word : textWords) {
			Query subquery = new QueryParser("text", this.analyzer).parse(word);
			queryBuilder.add(subquery, BooleanClause.Occur.FILTER);
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
