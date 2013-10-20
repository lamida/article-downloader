package net.lamida.nd.rest.neo;

import java.util.Date;

public interface IResultEntry {
	String getUrl();
	String getSnipet();
	String getStringDate();
	Date getDate();
	String getTitle(); 
	boolean isSelected();
	void setSelected(boolean b);
}