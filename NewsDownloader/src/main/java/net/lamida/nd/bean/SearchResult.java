package net.lamida.nd.bean;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	private String kind;
	private SearchInformation searchInformation;
	private List<SearchResultItem> items;
	
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}
	
	public void addResult(SearchResultItem item){
		getItems().add(item);
	}

	public SearchInformation getSearchInformation() {
		if(searchInformation == null){
			searchInformation = new SearchInformation();
		}
		return searchInformation;
	}

	public List<SearchResultItem> getItems() {
		if(items == null){
			items = new ArrayList<SearchResultItem>();
		}
		return items;
	}
}


