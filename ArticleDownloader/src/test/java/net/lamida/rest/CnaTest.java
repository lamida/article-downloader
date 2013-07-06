package net.lamida.rest;

import java.io.IOException;

import net.lamida.http.client.impl.cna.CnaSearchBuilder;
import net.lamida.rest.client.ISearchBuilder;

import org.junit.Before;
import org.junit.Test;

public class CnaTest {
	private Job job;
	@Before
	public void init() throws IOException{
		SearchParameter param = new SearchParameter();
		param.setQuery("java");
		job = new Job(param);
	}
	
	@Test
	public void testBuildUrl(){
		String queryUrl = "http://www.channelnewsasia.com/action/news/search/search/3014/search.do?dateRange=months&sortBy=latest&channelId=3012&query=mcdonald&pageNum=1";
		
		ISearchBuilder builder = new CnaSearchBuilder(job);
		builder.buildSearchResponse();
	}
}
