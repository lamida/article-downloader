package net.lamida.nd.pdf;

import java.util.Date;

import net.lamida.nd.Utils;
import net.lamida.nd.parser.IParser;
import net.lamida.nd.rest.neo.IResultEntry;
import net.lamida.nd.rest.neo.NewsImage;

public class PdfInputData {
	private String searchId;
	private String searchQuery;
	private String url;
	private String newsTitle;
	private String newsContent;
	private NewsImage newsImage;
	private Date newsPostDateTime;
	private static final String ERROR_PARSING = "Error Parsing";
	private int currentCount;
	private int selectedCount;
	private int wordsCount;
	
	public PdfInputData(String searchId, String searchQuery, IResultEntry entry, IParser parser, int currentCount, int selectedCount) {
		super();
		this.searchId = searchId;
		this.searchQuery = searchQuery;
		this.url = entry.getUrl();
		this.newsTitle = parser.getNewsTitle();
		this.newsContent = parser.getNewsContent();
		NewsImage image = new NewsImage();
    	image.setUrl(parser.getNewsImage());
    	image.setCaption(parser.getNewsImageCaption());
		this.newsImage = image;
		this.newsPostDateTime = entry.getDate();
		this.currentCount = currentCount;
		this.selectedCount = selectedCount;
		this.wordsCount = Utils.countWords(this.newsContent);
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
	
	public Date getNewsPostDateTime() {
		return newsPostDateTime;
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

	public String getSearchId() {
		return searchId;
	}

	public int getWordsCount() {
		return wordsCount;
	}
}