package net.lamida.nd.rest.neo;

public interface ISearchBuilder {
	// TODO: check logic checking when search end. Seems not necessary
	boolean buildResult(SearchResult searchResult, String html, int resultPerPage);
}
