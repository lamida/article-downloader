package net.lamida.nd.rest;


import junit.framework.Assert;

import org.junit.Test;

public class RestSearchTest {
	@Test
	public void test(){
		AljazeeraRestSearch search = new AljazeeraRestSearch();
		String result = search.execute("Egypt");
		Assert.assertNotNull(result);
		System.out.println(result);
	}
}
