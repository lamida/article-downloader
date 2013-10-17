package net.lamida.nd.rest.neo;

public interface IResultEntry {
	String getUrl();
	String getSnipet();
	String getDate();
	String getTitle(); 
	boolean isSelected();
	void setSelected(boolean b);
}