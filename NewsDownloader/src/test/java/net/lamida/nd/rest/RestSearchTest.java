package net.lamida.nd.rest;


import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.easymock.EasyMock;
import org.junit.Test;

public class RestSearchTest {
	@Test
	public void test() throws IOException{
		IRestSearch mock = EasyMock.createNiceMock(IRestSearch.class);
		EasyMock.expect(mock.execute("foo")).andReturn(FileUtils.readFileToString(new File("googleSearchAljazeera.txt")));
		EasyMock.replay(mock);
		String result = mock.execute("foo");
		Assert.assertNotNull(result);
		System.out.println(result);
	}
}
