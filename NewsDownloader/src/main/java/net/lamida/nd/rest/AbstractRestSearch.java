package net.lamida.nd.rest;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class AbstractRestSearch implements IRestSearch {
	private Logger log = Logger.getLogger(this.getClass().toString());
	private String endPoint = "https://www.googleapis.com/customsearch/v1";
	private String apiKey = "AIzaSyCFii2JdR0hbrPzUCEp2JBlB6Dit6GpXmY";
	protected String customSearchEngine;
	private int resultStart;
	private String sort;
	
	
	/* (non-Javadoc)
	 * @see net.lamida.nd.rest.IRestSearch#execute(java.lang.String)
	 */
	public String execute(String searchQuery) {
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
}
