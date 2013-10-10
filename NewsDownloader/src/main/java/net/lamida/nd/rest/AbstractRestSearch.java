package net.lamida.nd.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.lamida.nd.Constant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class AbstractRestSearch implements IRestSearch {
	private Log log = LogFactory.getLog(this.getClass().toString());
	private String endPoint = "https://www.googleapis.com/customsearch/v1";
	private String apiKey = "AIzaSyCFii2JdR0hbrPzUCEp2JBlB6Dit6GpXmY";
	protected String customSearchEngine;
	private int resultStart;
	private String sort;
	protected String searchQuery;
	protected String dateFrom;
	protected String dateTo;
	
	DateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	public static IRestSearch getSearchProvider(SearchProviderEnum searchProviderEnum) {
		IRestSearch search = null;
		switch (searchProviderEnum) {
		case ALJAZEERA:
			search = new AljazeeraRestSearch();
			break;
		case CNA:
			search = new CnaRestSearch();
			break;
		case CNN:
			search = new CnnRestSearch();
			break;
		default:
			break;
		}
		return search;
	}
	
	/* (non-Javadoc)
	 * @see net.lamida.nd.rest.IRestSearch#execute(java.lang.String)
	 */
	public String execute() {
		if(searchQuery == null){
			throw new IllegalStateException("set searchQuery first");
		}
		ClientRequest req = getClientRequest(searchQuery);
		
		String result = null;
		try {
			ClientResponse<String> res = req.get(String.class);
			result = res.getEntity();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}

	protected ClientRequest getClientRequest(String searchQuery) {
		ClientRequest req = new ClientRequest(endPoint);
		req.queryParameter("q", searchQuery)
			.queryParameter("key", apiKey)
			.queryParameter("cx", customSearchEngine);
		
		if(resultStart != 0){
			req.queryParameter("start", resultStart);
		}
		
		if(dateFrom != null){
			if(dateTo == null){
				dateTo = format.format(new Date());
			}
			sort = "date:r:" + dateFrom + ":" + dateTo;
		}
		
		if(sort != null){
			req.queryParameter("sort", sort);
		}
		
		return req;
	}

	public void setResultStart(int resultStart) {
		this.resultStart = resultStart;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String next() {
		resultStart += Constant.RESULTS_PER_PAGE;
		return execute();
	}

	public String prev() {
		if(resultStart <= 1){
			resultStart = 0;
			return execute();
		}else{
			resultStart -= Constant.RESULTS_PER_PAGE;
			return execute();
		}
	}

	public String goTo(int page) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setQuery(String query) {
		this.searchQuery = query;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
}
