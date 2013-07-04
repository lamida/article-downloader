package net.lamida.nd.rest;

import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class AljazeeraRestSearch extends AbstractRestSearch{
	private Logger log = Logger.getLogger(this.getClass().toString());
	private String newsPath = "http://www.aljazeera.com/news/";
	
	public AljazeeraRestSearch() {
		this.customSearchEngine = "007808206270820318552:kn2qoclkjhm";
	}
	
	public String execute(String searchQuery) {
		ClientRequest req = getClientRequest(searchQuery);
		req.queryParameter("siteSearch", newsPath);
		
		String result = null;
		try {
			ClientResponse<String> res = req.get(String.class);
			result = res.getEntity();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}
}
