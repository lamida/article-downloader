package net.lamida.nd.rest;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class AbstractRestSearch {
	private Logger log = Logger.getLogger(this.getClass().toString());
	private String endPoint = "https://www.googleapis.com/customsearch/v1";
	private String apiKey = "AIzaSyCFii2JdR0hbrPzUCEp2JBlB6Dit6GpXmY";
	protected String customSearchEngine;
	
	
	
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
		return req;
	}

}
