package net.lamida.nd.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.lamida.nd.Constant;
import net.lamida.nd.Utils;
import net.lamida.nd.bean.GoogleSearchResult;

public class LoadedSearch {
	private IRestSearch currentSearch;
	private Map<Integer, GoogleSearchResult> searchCache;
	private int resultStart = 1;
	private int loadedResult;
	private boolean dummyData;
	private SearchResultBuilder resultBuilder = new SearchResultBuilder();
	
	public LoadedSearch(IRestSearch currentSearch){
		this.currentSearch = currentSearch;
		searchCache = new HashMap<Integer, GoogleSearchResult>();
	}

	public LoadedSearch(){
		this.dummyData = false;
		searchCache = new HashMap<Integer, GoogleSearchResult>();
	}
	
	public void execute(){
		searchCache.clear();
		if (dummyData) {
			GoogleSearchResult searchResult = getDummySearchResult();
			searchCache.put(resultStart, searchResult);
		} else {
			System.out.println("Searching using provider: " + currentSearch.getClass().getSimpleName());
			String json = currentSearch.execute();
			GoogleSearchResult searchResult = resultBuilder.build(json);
			searchCache.put(resultStart, searchResult);
		}
		loadedResult += Constant.RESULTS_PER_PAGE;
	}
	
	public void navigateNext(){
		resultStart += Constant.RESULTS_PER_PAGE;
      if (searchCache.get(resultStart) == null) {
          if (dummyData) {
              GoogleSearchResult searchResult = getDummySearchResult();
              searchCache.put(resultStart, searchResult);
          } else {
              String json = currentSearch.next();
              GoogleSearchResult searchResult = resultBuilder.build(json);
              searchCache.put(resultStart, searchResult);
          }
          loadedResult += Constant.RESULTS_PER_PAGE;
      }
	}
	
	public void navigatePrev(){
		resultStart -= Constant.RESULTS_PER_PAGE;
        if (searchCache.get(resultStart) == null) {
            if (dummyData) {
                GoogleSearchResult searchResult = getDummySearchResult();
                searchCache.put(resultStart, searchResult);
            } else {
                String json = currentSearch.prev();
                GoogleSearchResult searchResult = resultBuilder.build(json);
                searchCache.put(resultStart, searchResult);
            }
            loadedResult += Constant.RESULTS_PER_PAGE;
        }
	}
	
	public GoogleSearchResult getCurrentSearchResult(){
		return searchCache.get(resultStart);
	}

	public GoogleSearchResult getNextSearchResult(int resultStart){
		if(searchCache.get(resultStart) == null){
			navigateNext(); 
		}
		return searchCache.get(resultStart);
	}
	
	private GoogleSearchResult getDummySearchResult() {
        SearchResultBuilder builder = new SearchResultBuilder();
        GoogleSearchResult result = null;
        try {
            result = builder.build(Utils.readFileToString(new File("resources/googleSearchCnn.txt")));
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return result;
    }
	

	public Map<Integer, GoogleSearchResult> getSearchCache() {
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
