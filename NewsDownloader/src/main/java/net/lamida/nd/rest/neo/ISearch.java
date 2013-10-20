package net.lamida.nd.rest.neo;



public interface ISearch {
	void init(String id, String keywords, ResultPerPage resultPerPage, SortBy sortBy);
	boolean search();
	boolean next();
	boolean prev();
	int getResultPerPage();
	int getCurrentResultStart();
	String getSearchMetaInfo();
	SearchResult getSearchResult();
	String getSearchId();
}