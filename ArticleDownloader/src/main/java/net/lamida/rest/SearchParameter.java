package net.lamida.rest;

public class SearchParameter {
	private String provider;
	private String query;
	private String endPoint;
	private String apiKey;
	private String format;
	private String pageSize;
	private String orderBy;
	private String orderByList;
	private String fromDate;
	private String toDate;
	// 
	private boolean countKeyWords;
	private boolean highlightKeyWords;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(String orderByList) {
		this.orderByList = orderByList;
	}

	public boolean isCountKeyWords() {
		return countKeyWords;
	}

	public void setCountKeyWords(boolean countWords) {
		this.countKeyWords = countWords;
	}

	public boolean isHighlightKeyWords() {
		return highlightKeyWords;
	}

	public void setHighlightKeyWords(boolean highlightKeyWords) {
		this.highlightKeyWords = highlightKeyWords;
	}
}
