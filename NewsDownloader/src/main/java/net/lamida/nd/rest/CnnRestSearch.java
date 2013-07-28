package net.lamida.nd.rest;

import net.lamida.nd.Utils;

public class CnnRestSearch extends AbstractRestSearch{

	public CnnRestSearch() {
		this.customSearchEngine = Utils.loadConfiguration("cnnRestSearchCustomSearchEngine");
	}
	
}
