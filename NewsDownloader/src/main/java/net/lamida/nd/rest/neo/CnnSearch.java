package net.lamida.nd.rest.neo;
import java.net.URL;

import net.lamida.nd.Utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CnnSearch extends AbstractSearch {
	private Log log = LogFactory.getLog(this.getClass().toString());
	public static void main(String[] args) throws Exception{
		System.out.println(SortBy.ALJAZEERA_DATE);
//		String keywords = "indonesia";
//		AbstractSearch search = new CnnSearch();
//		search.init(keywords,ResultPerPage.TEN, SortBy.CNN_DATE);
//		search.search();
//		for(IResultEntry entry:search.getSearchResult().getResultList()){
//			System.out.println(entry);
//		}
//		System.out.println(search.getSearchMetaInfo());
//		System.out.println();
//		System.out.println();
//		System.out.println(search.next());;
//		for(IResultEntry entry:search.getSearchResult().getResultList()){
//			System.out.println(entry);
//		}
//		System.out.println(search.getSearchMetaInfo());
	}
	
	final String urlTemplate = Utils.loadConfiguration("cnnSearchPath");
	
	public CnnSearch(){
		this.searchBuilder = new CnnSearchBuilder();
	}
	
	public void init(String keywords, ResultPerPage resultPerPage, SortBy sortBy){
		this.keywords = keywords;
		this.resultPerPage = ResultPerPage.TEN;
		this.sortBy = sortBy;
		this.start = 1;
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
		start += resultPerPage.getTotal();
		return search();
	}

	public boolean prev(){
		// NOT IMPLEMENTED
		return false;
	}
	
	private String buildUrl() {
		String query = keywords;
		String[] qs = query.split(" ");
		if(qs.length > 1){
			StringBuffer sb = new StringBuffer();
			for(String s: qs){
				sb.append(s);
				sb.append("+");
			}
			query = sb.substring(0, sb.toString().length() - 1);
		}
		return urlTemplate.replace("<<query>>", query)
								.replace(SortBy.enumLabel, sortBy.getValue())
								.replace("<<start>>", start + "")
								;
	}
}
