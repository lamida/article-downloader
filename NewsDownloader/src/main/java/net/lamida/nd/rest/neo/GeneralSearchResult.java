package net.lamida.nd.rest.neo;

import java.util.Date;


public class GeneralSearchResult implements IResultEntry {
	private String url;
	private String title;
	private String snipet;
	private String stringDate;
	private Date dt;
	private boolean selected;

	public GeneralSearchResult(String url, String title, String snipet, String date, Date dt) {
		super();
		this.url = url;
		this.title = title;
		this.snipet = snipet;
		this.stringDate = date;
		this.dt = dt;
	}

	public String getUrl() {
		return url;
	}

	public String getSnipet() {
		return snipet;
	}

	public String getStringDate() {
		return stringDate;
	}

	public String getTitle() {
		return title;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneralSearchResult other = (GeneralSearchResult) obj;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CnaResult [title=" + title + ", url=" + url + ", snipet="
				+ snipet + ", date=" + stringDate + "]";
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Date getDate() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}
	
}
