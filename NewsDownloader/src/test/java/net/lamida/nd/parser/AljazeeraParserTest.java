package net.lamida.nd.parser;

import junit.framework.Assert;
import net.lamida.nd.parser.AljazeeraParser;

import org.junit.Before;
import org.junit.Test;

public class AljazeeraParserTest {
	private String url;
	
	@Before
	public void init(){
		url = "http://www.aljazeera.com/news/asia/2013/06/20136234309866467.html";
	}
	
	@Test
	public void testParse(){
		AljazeeraParser parser = new AljazeeraParser();
		parser.init(url);
		String articleText = parser.getNewsContent();
		Assert.assertNotNull(articleText);
		
		String articleTitle = parser.getNewsTitle();
		Assert.assertNotNull(articleTitle);
		Assert.assertEquals("Pakistani Taliban claim attack on foreigners", articleTitle);
		
		String articleSection = parser.getNewsSection();
		Assert.assertNotNull(articleSection);
		Assert.assertEquals("Central & South Asia", articleSection);
		
		String articlePostTime = parser.getNewsPostTime();
		Assert.assertNotNull(articlePostTime);
		Assert.assertEquals("24 Jun 2013 05:50", articlePostTime);
	}
}
