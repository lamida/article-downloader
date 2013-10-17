package net.lamida.nd.rest.neo;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AljazeeraHtmlSearchBuilder implements ISearchBuilder{
	public boolean buildResult(SearchResult searchResult, String html, int resultPerPage) {
		boolean resultStillAvailable = false;
		Document doc = Jsoup.parse(html); 
		Elements el = doc.select("div.indexText-Bold2");
		Iterator<Element> it = el.iterator();
		Element elCount = doc.select("span.bluetext").get(0);
		searchResult.setSearchMetaInfo(elCount.text());
		searchResult.setTotalResult(Integer.parseInt(buildSearchCount(elCount.text())));
		int i = 0;
		while(it.hasNext()){
			resultStillAvailable = true;
			Element rowEl = it.next();
			Element linkEl = rowEl.select("a").get(0);
			Element titleEl = rowEl.select("b").get(0);
			Element snipetEl = rowEl.select("div.indexSummaryText").get(0);
			Element dateEl = rowEl.select("div.indexSmallText").get(0);
			String link = linkEl.attr("href");
			String title = titleEl.text();
			String date = dateEl.text();
			String snipet = snipetEl.text();
			IResultEntry result = new GeneralSearchResult(link, title, snipet, date);
			if(i == 0 && searchResult.getResultList().size() >= resultPerPage){
				int size = searchResult.getResultList().size();
				if(result.equals(searchResult.getResultList().get(size - resultPerPage  + 1))){
					resultStillAvailable = false;
					break;
				}
			}
			searchResult.getResultList().add(result);
			i++;
		}
		return resultStillAvailable;
	}
	
	private String buildSearchCount(String fragment){
		int start = fragment.indexOf("about ") + "about ".length();
		int end = fragment.indexOf(". Search took");
		return fragment.substring(start, end);
		
	}
}

