package net.lamida.nd.rest;

import java.util.Iterator;

import javax.swing.JOptionPane;

import net.lamida.nd.bean.SearchInformation;
import net.lamida.nd.bean.SearchResult;
import net.lamida.nd.bean.SearchResultItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SearchResultBuilder {
	public SearchResult build(String json){
		if(json == null){
			throw new IllegalArgumentException("json string have to set first");
		}
		JsonParser parser = new JsonParser();
		SearchResult result = new SearchResult();
		JsonObject root = parser.parse(json).getAsJsonObject();
		result.setKind(root.get("kind").getAsString());
		
		JsonObject searchInfoJson = root.get("searchInformation").getAsJsonObject();
		SearchInformation searchInfo = result.getSearchInformation();
		searchInfo.setSearchTime(searchInfoJson.get("searchTime").getAsString());
		searchInfo.setTotalResults(searchInfoJson.get("totalResults").getAsLong());
		
		result.getItems();
		JsonElement element = root.get("items");
		if(element != null){
			JsonArray jsonItems = element.getAsJsonArray();
			Iterator<JsonElement> iterator = jsonItems.iterator();
			while(iterator.hasNext()){
				JsonObject item = iterator.next().getAsJsonObject();
				SearchResultItem searchItem = new SearchResultItem();
				searchItem.setTitle(item.get("title").getAsString());
				searchItem.setLink(item.get("link").getAsString());
				searchItem.setSnippet(item.get("snippet").getAsString());
				result.addResult(searchItem);
			}
		}else{
			JOptionPane.showMessageDialog(null, "No Result!");
		}
		return result;
	}
}
