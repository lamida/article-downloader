package net.lamida.nd.rest.neo;

import net.lamida.nd.rest.SearchProviderEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractSearch implements ISearch{
	private Log log = LogFactory.getLog(this.getClass().toString());
	protected String searchId;
	
	public static ISearch getSearchProvider(SearchProviderEnum searchProviderEnum) {
		ISearch search = null;
		switch (searchProviderEnum) {
		case ALJAZEERA:
			search = new AljazeeraSearch();
			break;
		case CNA:
			search = new CnaSearch();
			break;
		case CNN:
			search = new CnnSearch();
			break;
		default:
			break;
		}
		return search;
	}
	
	protected boolean availableNext;
	protected int start;
	protected String keywords;
	protected ResultPerPage resultPerPage;
	protected SortBy sortBy;
	protected SearchResult searchResult = new SearchResult();
	protected ISearchBuilder searchBuilder;
	
	public abstract void init(String id, String keywords, ResultPerPage resultPerPage, SortBy sortBy);

	public abstract boolean search();

	public abstract boolean next();

	public abstract boolean prev();

	public int getResultPerPage(){
		return resultPerPage.getTotal();
	}
	
	public int getCurrentResultStart(){
		return start;
	}
	
	public String getSearchMetaInfo(){
		return searchResult.getSearchMetaInfo();
	}
	
	public SearchResult getSearchResult() {
		return searchResult;
	}

	public String getSearchId() {
		return searchId;
	}
}
