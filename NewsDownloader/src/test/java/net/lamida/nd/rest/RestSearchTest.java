package net.lamida.nd.rest;


import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import net.lamida.nd.Utils;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Test;

public class RestSearchTest {
	//@Test
	public void test() throws IOException{
		IRestSearch mock = EasyMock.createNiceMock(IRestSearch.class);
		EasyMock.expect(mock.execute()).andReturn(Utils.readFileToString(new File("googleSearchAljazeera.txt")));
		EasyMock.replay(mock);
		String result = mock.execute();
		Assert.assertNotNull(result);
		System.out.println(result);
	}
	
	@Test
	public void pagingTest() throws IOException{
		IRestSearch search = AbstractRestSearch.getSearchProvider(SearchProviderEnum.CNA);
		search.setQuery("korea");
		search.setDateFrom("20130601");
		search.setDateTo("20130630");
		String json = search.execute();
		FileUtils.writeStringToFile(new File("page-one.txt"), json);
		json = search.next();
		FileUtils.writeStringToFile(new File("page-two.txt"), json);
	}
}
