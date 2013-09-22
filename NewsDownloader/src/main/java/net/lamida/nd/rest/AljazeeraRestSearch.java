package net.lamida.nd.rest;

import net.lamida.nd.Utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class AljazeeraRestSearch extends AbstractRestSearch{
	private Log log = LogFactory.getLog(this.getClass().toString());
	private String newsPath = Utils.loadConfiguration("aljazeeraRestSearchNewsPath");
	
	public AljazeeraRestSearch() {
		this.customSearchEngine = Utils.loadConfiguration("aljazeeraRestSearchCustomSearchEngine");
	}
	
	public String execute() {
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
