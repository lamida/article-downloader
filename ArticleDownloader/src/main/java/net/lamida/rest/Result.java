package net.lamida.rest;

import java.util.Date;

public class Result {
	private String id;
	private String sectionId;
	private String sectionName;
	private String webPublicationDate;
	private String webTitle;
	private String webUrl;
	private String apiUrl;
	// 
//	private String downloadedFile;
	private Date retrievedDate;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getWebPublicationDate() {
		return webPublicationDate;
	}

	public void setWebPublicationDate(String webPublicationDate) {
		this.webPublicationDate = webPublicationDate;
	}

	public String getWebTitle() {
		return webTitle;
	}

	public void setWebTitle(String webTitle) {
		this.webTitle = webTitle;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

//	public String getDownloadedFile() {
//		return downloadedFile;
//	}
//
//	public void setDownloadedFile(String downloadedFile) {
//		this.downloadedFile = downloadedFile;
//	}

	public Date getRetrievedDate() {
		return retrievedDate;
	}

	public void setRetrievedDate(Date retrievedDate) {
		this.retrievedDate = retrievedDate;
	}
}
