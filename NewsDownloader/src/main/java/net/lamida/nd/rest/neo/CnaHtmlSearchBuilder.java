package net.lamida.nd.rest.neo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CnaHtmlSearchBuilder implements ISearchBuilder{
	final String dateFormat = "d MMM yyyy";
	public boolean buildResult(SearchResult searchResult, String html, int resultPerPage) {
		boolean resultStillAvailable = false;
		Document doc = Jsoup.parse(html.toString());
		Elements elRecord = doc.select("li div.holder div.txt-box a");
		Elements elCount = doc.select("div.archive-pagination span.num");
		searchResult.setSearchMetaInfo(elCount.text() + " Search Result");
		searchResult.setTotalResult(Integer.parseInt(elCount.text()));
		Iterator<Element> it = elRecord.iterator();
		int i = 0;
		while(it.hasNext()){
			resultStillAvailable = true;
			it.next();
			Element linkEl = doc.select("div.txt-box a").get(i);
			Element snipetEl = doc.select("div.txt-box p").get(i);
			Element dateEl = doc.select("div.date-box span.date").get(i);
			String link = "http://www.channelnewsasia.com" + linkEl.attr("href");
			String title = linkEl.text();
			String stringDate = dateEl.text();
			Date date = null;
			try {
				date = new SimpleDateFormat(dateFormat).parse(stringDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String snipet = snipetEl.text();
			IResultEntry result = new GeneralSearchResult(link, title, snipet, stringDate, date);
			if(i == 0 && searchResult.getResultList().size() >= resultPerPage){
				int size = searchResult.getResultList().size();
				if(result.equals(searchResult.getResultList().get(size - resultPerPage + 1))){
					resultStillAvailable = false;
					break;
				}
			}
			searchResult.getResultList().add(result);
			i++;
		}
		return resultStillAvailable;
	}
}

