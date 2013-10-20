package net.lamida.nd.rest.neo;
import java.net.URL;

import net.lamida.nd.Utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO refactor to proper oo design
 * @author lamida
 *
 */
public class CnaSearch extends AbstractSearch {
	private Log log = LogFactory.getLog(this.getClass().toString());
	public static void main(String[] args) throws Exception{
		String keywords = "indonesia haze";
		CnaSearch search = new CnaSearch();
		search.init("foo", keywords,ResultPerPage.TEN, SortBy.CNA_LATEST);
		search.search();
		for(IResultEntry entry:search.getSearchResult().getResultList()){
			System.out.println(entry);
		}
		System.out.println(search.getSearchMetaInfo());
		System.out.println(search.next());;
		for(IResultEntry entry:search.getSearchResult().getResultList()){
			System.out.println(entry);
		}
		System.out.println(search.getSearchResult().getTotalResult());
//		search.next();
//		for(IResultEntry entry:search.getSearchResult().getResultList()){
//			System.out.println(entry);
//		}
	}
	
	final String urlTemplate = Utils.loadConfiguration("cnaSearchPath");
	
	public CnaSearch(){
		this.searchBuilder = new CnaHtmlSearchBuilder();
	}
	
	public void init(String id, String keywords, ResultPerPage resultPerPage, SortBy sortBy){
		this.searchId = id;
		this.keywords = keywords;
		this.resultPerPage = ResultPerPage.TEN;
		this.sortBy = sortBy;
		this.searchResult = new SearchResult();
	}
	
	/**
	 * @param keywords
	 */
	public boolean search() {
		boolean resultAvailable = true;
		try{
			String url = buildUrl();
			log.info(url);
			String html = IOUtils.toString(new URL(url));
			resultAvailable = searchBuilder.buildResult(searchResult, html, resultPerPage.getTotal());
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultAvailable;
	}

	public boolean next(){
		start++;
		return search();
	}

	public boolean prev(){
		// NOT IMPLEMENTED
		return false;
	}
	
	private String buildUrl() {
		System.out.println("sortBy: " + sortBy.getValue());
		String query = keywords;
		String[] qs = query.split(" ");
		if(qs.length > 1){
			StringBuffer sb = new StringBuffer();
			for(String s: qs){
				sb.append(s);
				sb.append("%20");
			}
			query = sb.substring(0, sb.toString().length() - 3);
		}
		return urlTemplate.replace("<<query>>", query)
								.replace(DocType.title, DocType.ARTICLES_ONLY.label)
								.replace(Range.title, Range.THREE_MONTHS.label)
								.replace(SortBy.enumLabel, sortBy.getValue())
								.replace("<<pageNum>>", start + "")
								;
	}
	
	enum DocType{
		ALL(""), ARTICLES_ONLY("MCNewsArticle"), VIDEOS_ONLY("MCVideo");
		static String title = "<<docType>>";
		private String label;
		DocType(String label){
			this.label =label;
		}
		public String getLabel(){
			return label;
		}
	};
	enum Range{
		SEVEN_DAYS("days"), THIRTY_DAYS("weeks"), THREE_MONTHS("months");
		static String title = "<<dateRange>>";
		private String label;
		Range(String label){
			this.label =label;
		}
		public String getLabel(){
			return label;
		}
	};
}
