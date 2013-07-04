package net.lamida.nd.rest;

import java.util.Iterator;

import net.lamida.nd.bean.SearchInformation;
import net.lamida.nd.bean.SearchResult;
import net.lamida.nd.bean.SearchResultItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SearchResultBuilder {
	private String json;
	
	public void setJson(String json) {
		this.json = json;
	}

	public void build(){
		JsonParser parser = new JsonParser();
		SearchResult result = new SearchResult();
		JsonObject root = parser.parse(json).getAsJsonObject();
		result.setKind(root.get("kind").getAsString());
		
		JsonObject searchInfoJson = root.get("searchInformation").getAsJsonObject();
		SearchInformation searchInfo = result.getSearchInformation();
		searchInfo.setSearchTime(searchInfoJson.get("searchTime").getAsString());
		searchInfo.setTotalResults(searchInfoJson.get("totalResults").getAsLong());
		
		result.getItems();
		JsonArray jsonItems = root.get("items").getAsJsonArray();
		Iterator<JsonElement> iterator = jsonItems.iterator();
		while(iterator.hasNext()){
			JsonObject item = iterator.next().getAsJsonObject();
			SearchResultItem searchItem = new SearchResultItem();
			searchItem.setTitle(item.get("title").getAsString());
			searchItem.setLink(item.get("link").getAsString());
		}
	}
}
