package net.lamida.nd.rest.neo;

import java.util.ArrayList;
import java.util.List;

public class SearchResult{
	private int totalResult;
	private String searchMetaInfo;
	List<IResultEntry> resultList;
	private int prevResult;

	public SearchResult() {
		resultList = new ArrayList<IResultEntry>();
	}
	
	public void setTotalResult(int totalResult) {
		this.totalResult = totalResult;
	}

	public int getTotalResult() {
		return totalResult;
	}

	public String getSearchMetaInfo() {
		return searchMetaInfo;
	}

	public void setSearchMetaInfo(String searchMetaInfo) {
		this.searchMetaInfo = searchMetaInfo;
	}

	public List<IResultEntry> getResultList() {
		return resultList;
	}

	public int getPrevResult() {
		return prevResult;
	}

	public void setPrevResult(int prevResult) {
		this.prevResult = prevResult;
	}
}
