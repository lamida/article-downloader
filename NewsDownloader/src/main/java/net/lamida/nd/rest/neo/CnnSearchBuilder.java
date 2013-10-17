package net.lamida.nd.rest.neo;

import java.util.Date;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CnnSearchBuilder implements ISearchBuilder{
	public boolean buildResult(SearchResult searchResult, String html, int resultPerPage) {
		Document doc = Jsoup.parse(html.toString());
		String json = doc.select("#jsCode").text();
		return parseJson(searchResult, json, resultPerPage);
	}
	
	public boolean parseJson(SearchResult searchResult, String json, int resultPerPage){
		boolean resultStillAvailable = false;
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(json);
		
		String meta = el.getAsJsonObject().getAsJsonObject("metaResults").getAsJsonPrimitive("article").getAsString();
		
		searchResult.setSearchMetaInfo(meta + " Search Result");
		searchResult.setTotalResult(Integer.parseInt(meta));
		JsonArray array = el.getAsJsonObject().getAsJsonArray("results");
		Iterator<JsonElement> it =  array.get(0).getAsJsonArray().iterator();
		int i = 0;
		while(it.hasNext()){
			resultStillAvailable = true;
			JsonElement row = it.next();
			String link = row.getAsJsonObject().getAsJsonPrimitive("url").getAsString();
			String title = row.getAsJsonObject().getAsJsonPrimitive("title").getAsString();
			String snipet = row.getAsJsonObject().getAsJsonObject("metadata").getAsJsonObject("media").getAsJsonPrimitive("excerpt").getAsString();
			String date = row.getAsJsonObject().getAsJsonPrimitive("mediaDateUts").getAsString();
			Date d = new Date(Long.parseLong(date) * 1000);
			date = d.toString();
			IResultEntry result = new GeneralSearchResult(link, title, snipet, date);
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
