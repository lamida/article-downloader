package net.lamida.nd.pdf;

public class PdfInputData {
	private String searchQuery;
	private String url;
	private String newsTitle;
	private String newsContent;
	private String newsPostTime;
	private static final String ERROR_PARSING = "Error Parsing";
	
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
		if(newsTitle == null){
			newsTitle = ERROR_PARSING;
		}
		return newsTitle;
	}
	public String getNewsContent() {
		if(newsContent == null){
			newsContent = ERROR_PARSING;
		}
		return newsContent;
	}
	public String getNewsPostTime() {
		if(newsPostTime == null){
			newsPostTime = ERROR_PARSING;
		}
		return newsPostTime;
	}
}