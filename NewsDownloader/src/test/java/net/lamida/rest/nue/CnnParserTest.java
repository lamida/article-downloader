package net.lamida.rest.nue;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class CnnParserTest {
	private String url;
	
	@Before
	public void init(){
		url = "http://edition.cnn.com/2013/07/02/world/meast/egypt-protests";
	}
	
	@Test
	public void testParse(){
		CnnParser parser = new CnnParser();
		parser.init(url);
		String articleText = parser.getNewsContent();
		Assert.assertNotNull(articleText);
		
		String articleTitle = parser.getNewsTitle();
		Assert.assertNotNull(articleTitle);
		Assert.assertEquals("Showdown? Egypt's Morsy defies military 'ultimatum'", articleTitle);
		
		String articleSection = parser.getNewsSection();
		Assert.assertNotNull(articleSection);
		Assert.assertEquals("Middle East", articleSection);
		
		String articlePostTime = parser.getNewsPostTime();
		Assert.assertNotNull(articlePostTime);
		Assert.assertEquals("July 3, 2013 -- Updated 0339 GMT (1139 HKT)", articlePostTime);
	}
}
