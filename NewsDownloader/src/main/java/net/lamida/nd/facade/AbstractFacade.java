package net.lamida.nd.facade;

import net.lamida.nd.rest.AljazeeraRestSearch;
import net.lamida.nd.rest.CnaRestSearch;
import net.lamida.nd.rest.CnnRestSearch;
import net.lamida.nd.rest.IRestSearch;
import net.lamida.nd.rest.SearchProviderEnum;

public class AbstractFacade {
	private String query;
	private SearchProviderEnum searchProviderEnum;
	private IRestSearch searchProvider;
	public void process(){
		if(query == null || searchProviderEnum == null){
			throw new IllegalArgumentException();
		}
		IRestSearch search = null;
		//search = getSearchProvider(search);
		
		String result = search.execute();
	}
	
	public void setSearchProvider(IRestSearch restSearch) {
		this.searchProvider = restSearch;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
