package net.lamida.nd.bean;

public class SearchInformation {
	public String searchTime;
	public long totalResults;

	public SearchInformation() {
	}

	public String getSearchTime() {
		return searchTime;
	}

	public void setSearchTime(String searchTime) {
		this.searchTime = searchTime;
	}

	public long getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}
	
	
}