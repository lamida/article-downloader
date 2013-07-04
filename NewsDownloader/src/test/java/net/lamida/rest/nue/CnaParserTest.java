package net.lamida.rest.nue;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class CnaParserTest {
	private String url;
	
	@Before
	public void init(){
		url = "http://www.channelnewsasia.com/news/world/egypt-s-army-topples/733606.html";
	}
	
	@Test
	public void testParse(){
		CnaParser parser = new CnaParser();
		parser.init(url);
		String articleText = parser.getNewsContent();
		Assert.assertNotNull(articleText);
		
		String articleTitle = parser.getNewsTitle();
		Assert.assertNotNull(articleTitle);
		Assert.assertEquals("Egypt's army topples president Morsi", articleTitle);
		
		String articleSection = parser.getNewsSection();
		Assert.assertNotNull(articleSection);
		Assert.assertEquals("World", articleSection);
		
		String articlePostTime = parser.getNewsPostTime();
		Assert.assertNotNull(articlePostTime);
		Assert.assertEquals("04 Jul 2013 3:34 AM", articlePostTime);
	}
	
	
}
