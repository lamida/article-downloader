package net.lamida.nd.rest;

import net.lamida.nd.Utils;

public class CnaRestSearch extends AbstractRestSearch{

	public CnaRestSearch() {
		this.customSearchEngine = Utils.loadConfiguration("cnaRestSearchCustomSearchEngine");
	}
	
}
