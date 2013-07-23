package net.lamida.nd.rest;

public interface IRestSearch {
	void setQuery(String query);
	void setDateFrom(String dateFrom);
	void setDateTo(String dateTo);
	public abstract String execute();
	public abstract String next();
	public abstract String prev();
	public abstract String goTo(int page);
}