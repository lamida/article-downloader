package net.lamida.nd.rest.neo;
import java.net.URL;

import net.lamida.nd.Utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AljazeeraSearch extends AbstractSearch{
	private Log log = LogFactory.getLog(this.getClass().toString());
	public static void main(String[] args) throws Exception{
		String keywords = "indonesia";
		AbstractSearch search = new AljazeeraSearch();
		search.init("foo", keywords, ResultPerPage.FIFTEEN, SortBy.ALJAZEERA_RELEVANCE);
		search.search();
		for(IResultEntry entry:search.getSearchResult().getResultList()){
			System.out.println(entry);
		}
		System.out.println(search.getSearchMetaInfo());
		System.out.println();
		search.init("bar", keywords, ResultPerPage.FIFTEEN, SortBy.ALJAZEERA_DATE);
		System.out.println(search.search());
		for(IResultEntry entry:search.getSearchResult().getResultList()){
			System.out.println(entry);
		}
		System.out.println(search.getSearchMetaInfo());
	}
	
	final String urlTemplate = Utils.loadConfiguration("aljazeeraSearchPath");
	
	public AljazeeraSearch(){
		searchBuilder = new AljazeeraHtmlSearchBuilder();
	}
	
	public void init(String id, String keywords, ResultPerPage resultPerPage, SortBy sortBy){
		this.searchId = id;
		this.keywords = keywords;
		this.resultPerPage = ResultPerPage.FIFTEEN;
		this.sortBy = sortBy;
		this.searchResult = new SearchResult();
	}
	
	public boolean search() {
		boolean resultAvailable = true;
		try{
			String url = buildUrl();
			log.info(url);
			String escapedJsContent = IOUtils.toString(new URL(url));
			String html = unescapeJsFragment(escapedJsContent);
			resultAvailable = searchBuilder.buildResult(searchResult, html, resultPerPage.getTotal());
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultAvailable;
	}

	/**
	 * @param keywords
	 */
	public boolean next(){
		start += resultPerPage.getTotal();
		return search();
	}

	public boolean prev(){
		// NOT IMPLEMENTED
		return false;
	}

	private String unescapeJsFragment(String jsFragment){
		int start = jsFragment.indexOf("unescape(") + "unescape(".length();
		int end = jsFragment.indexOf(");showOrHide()");
		String htmlUnescape = jsFragment.substring(start + 1, end - 1);
		return StringEscapeUtils.unescapeJava(htmlUnescape);
	}
	
	private String buildUrl() {
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
								.replace(SortBy.enumLabel, sortBy.getValue())
								.replace("<<start>>", start + "")
								.replace("<<resultPerPage>>", resultPerPage.getTotal() + "")
								;
	}
}
