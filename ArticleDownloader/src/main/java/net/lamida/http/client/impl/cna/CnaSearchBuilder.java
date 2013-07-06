package net.lamida.http.client.impl.cna;

import java.io.IOException;
import java.util.Date;

import net.lamida.rest.Job;
import net.lamida.rest.SearchResponse;
import net.lamida.rest.SearchResult;
import net.lamida.rest.client.ISearchBuilder;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CnaSearchBuilder implements ISearchBuilder {
	private Logger log = Logger.getLogger(this.getClass().toString());
	private Job job;

	public static String DEFAULT_HTTP_PATH = "http://http://www.channelnewsasia.com";
	public static String SEARCH_HTTP_PATH = "http://www.channelnewsasia.com/action/news/search/search/3014/search.do?query={query}&pageNum=0&docType=MCNewsArticle&dateRange=months&sortBy=latest&pageNum=0&channelId=3012";
	public static String SEARCH_RESULT_LINK_SELECTOR = "h2 a";
	public static String SEARCH_COUNT_SELECTOR = "div.archive-pagination h2 span";
	public static String PAGER_URL_QUERY_PARAMETER = "pageNum";
	
	public CnaSearchBuilder(Job job) {
		super();
		this.job = job;
	}
	
	

	
	private int getSearchCount(Document doc){
		int count = 0;
		Elements els = doc.select(SEARCH_COUNT_SELECTOR);
		if(els != null){
			Element element = els.get(0);
			count = Integer.parseInt(element.text());
		}
		return count;
	}
	


	public SearchResponse buildSearchResponse() {
		log.info("Building List of articles...");
		
		String url = SEARCH_HTTP_PATH.replace("{query}", job.getParam().getQuery());
		
		SearchResponse searchResponse = new SearchResponse();
		try {
			Document doc =  Jsoup.connect(url).get();
			int searchCount = getSearchCount(doc);
			int pages = searchCount % 10;
			
			for(int i = 0; i < pages; i++){
				url = url.replace("pageNum=0", "pageNum=" + i);
				if(i != 0){
					doc =  Jsoup.connect(url).get();
				}
				Elements els = doc.select(SEARCH_RESULT_LINK_SELECTOR);
				for(Element el : els){
					SearchResult sr = new SearchResult();
					String link = el.attr("href");
					String title = el.attr("title");
					sr.setWebUrl(DEFAULT_HTTP_PATH + link);
					sr.setWebTitle(title);
					String[] id = link.split("/");
					sr.setSectionName(id[2]);
					sr.setId(id[3]);
					sr.setRetrievedDate(new Date());
					searchResponse.addSearchResults(sr);
				}
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return searchResponse;
	}
	
	public static void main(String[] args) {
		String s= "/news/asiapacific/school-exam-cheating/693778.html";
		String[]ss = s.split("/");
		for(String x : ss){
			System.out.println(x);
		}
	}

}
