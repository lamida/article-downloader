package net.lamida.nd.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.lamida.nd.Constant;
import net.lamida.nd.Utils;
import net.lamida.nd.bean.SearchResult;

public class LoadedSearch {
	private IRestSearch currentSearch;
	private Map<Integer, SearchResult> searchCache;
	private int resultStart = 1;
	private int loadedResult;
	private boolean dummyData;
	private SearchResultBuilder resultBuilder = new SearchResultBuilder();
	
	public LoadedSearch(IRestSearch currentSearch){
		this.currentSearch = currentSearch;
		searchCache = new HashMap<Integer, SearchResult>();
	}

	public LoadedSearch(){
		this.dummyData = true;
		searchCache = new HashMap<Integer, SearchResult>();
	}
	
	public void execute(){
		searchCache.clear();
		if (dummyData) {
			SearchResult searchResult = getDummySearchResult();
			searchCache.put(resultStart, searchResult);
		} else {
			System.out.println("Searching using provider: " + currentSearch.getClass().getSimpleName());
			String json = currentSearch.execute();
			SearchResult searchResult = resultBuilder.build(json);
			searchCache.put(resultStart, searchResult);
		}
		loadedResult += Constant.RESULTS_PER_PAGE;
	}
	
	public void navigateNext(){
		resultStart += Constant.RESULTS_PER_PAGE;
      if (searchCache.get(resultStart) == null) {
          if (dummyData) {
              SearchResult searchResult = getDummySearchResult();
              searchCache.put(resultStart, searchResult);
          } else {
              String json = currentSearch.next();
              SearchResult searchResult = resultBuilder.build(json);
              searchCache.put(resultStart, searchResult);
          }
          loadedResult += Constant.RESULTS_PER_PAGE;
      }
	}
	
	public void navigatePrev(){
		resultStart -= Constant.RESULTS_PER_PAGE;
        if (searchCache.get(resultStart) == null) {
            if (dummyData) {
                SearchResult searchResult = getDummySearchResult();
                searchCache.put(resultStart, searchResult);
            } else {
                String json = currentSearch.prev();
                SearchResult searchResult = resultBuilder.build(json);
                searchCache.put(resultStart, searchResult);
            }
            loadedResult += Constant.RESULTS_PER_PAGE;
        }
	}
	
	public SearchResult getCurrentSearchResult(){
		return searchCache.get(resultStart);
	}

	public SearchResult getNextSearchResult(int resultStart){
		if(searchCache.get(resultStart) == null){
			navigateNext(); 
		}
		return searchCache.get(resultStart);
	}
	
	private SearchResult getDummySearchResult() {
        SearchResultBuilder builder = new SearchResultBuilder();
        SearchResult result = null;
        try {
            result = builder.build(Utils.readFileToString(new File("resources/googleSearchCnn.txt")));
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return result;
    }
	

	public Map<Integer, SearchResult> getSearchCache() {
		return searchCache;
	}

	public int getResultStart() {
		return resultStart;
	}

	public void setResultStart(int resultStart) {
		this.resultStart = resultStart;
	}
	
	

	public int getLoadedResult() {
		return loadedResult;
	}
}
