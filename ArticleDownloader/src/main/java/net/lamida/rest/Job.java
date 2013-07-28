
package net.lamida.rest;

import java.util.Date;

import net.lamida.util.Utils;

public class Job {
	private String id;
	private SearchParameter param;
	private SearchResponse response;
	

	public Job(SearchParameter param) {
		super();
		this.param = param;
		this.id = Utils.formatDate(new Date());
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SearchParameter getParam() {
		return param;
	}

	public void setParam(SearchParameter param) {
		this.param = param;
	}

	public SearchResponse getResponse() {
		return response;
	}

	public void setResponse(SearchResponse response) {
		this.response = response;
	}

}
