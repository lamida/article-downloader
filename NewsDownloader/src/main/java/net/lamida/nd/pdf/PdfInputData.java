package net.lamida.nd.pdf;

public class PdfInputData {
	private String searchQuery;
	private String url;
	private String newsTitle;
	private String newsContent;
	private String newsPostTime;
	
	public PdfInputData(String searchQuery, String url, String newsTitle,
			String newsContent, String newsPostTime) {
		super();
		this.searchQuery = searchQuery;
		this.url = url;
		this.newsTitle = newsTitle;
		this.newsContent = newsContent;
		this.newsPostTime = newsPostTime;
	}
	public String getSearchQuery() {
		return searchQuery;
	}
	public String getUrl() {
		return url;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public String getNewsContent() {
		return newsContent;
	}
	public String getNewsPostTime() {
		return newsPostTime;
	}
}