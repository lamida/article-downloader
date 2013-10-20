package net.lamida.nd.pdf;

import net.lamida.nd.rest.neo.NewsImage;

public class PdfInputData {
	private String searchQuery;
	private String url;
	private String newsTitle;
	private String newsContent;
	private NewsImage newsImage;
	private String newsPostTime;
	private static final String ERROR_PARSING = "Error Parsing";
	private int currentCount;
	private int selectedCount;
	
	public PdfInputData(String searchQuery, String url, String newsTitle,
			String newsContent, String newsPostTime, NewsImage newsImage, int currentCount, int selectedCount) {
		super();
		this.searchQuery = searchQuery;
		this.url = url;
		this.newsTitle = newsTitle;
		this.newsContent = newsContent;
		this.newsImage = newsImage;
		this.newsPostTime = newsPostTime;
		this.currentCount = currentCount;
		this.selectedCount = selectedCount;
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
	
	public int getCurrentCount() {
		return currentCount;
	}
	
	public int getSelectedCount() {
		return selectedCount;
	}
	
	public NewsImage getNewsImage() {
		return newsImage;
	}
}