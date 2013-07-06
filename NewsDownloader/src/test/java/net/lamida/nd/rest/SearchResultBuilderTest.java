package net.lamida.nd.rest;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import net.lamida.nd.Utils;
import net.lamida.nd.bean.SearchResult;
import net.lamida.nd.bean.SearchResultItem;

import org.junit.Test;

public class SearchResultBuilderTest {
	@Test
	public void buildJsonResultTest() throws IOException{
		SearchResultBuilder builder = new SearchResultBuilder();
		String json = Utils.readFileToString(new File("googleSearchCnn.txt"));
		SearchResult result = builder.build(json);
		Assert.assertNotNull(result);
		Assert.assertEquals("customsearch#search", result.getKind());
		Assert.assertNotNull(result.getItems());
		Assert.assertTrue(!result.getItems().isEmpty());
		Assert.assertEquals(10, result.getItems().size());
		SearchResultItem itemOne = result.getItems().get(0);
		Assert.assertNotNull(itemOne);
		Assert.assertNotNull(itemOne.getLink());
		Assert.assertEquals("http://www.cnn.com/2013/07/02/world/meast/egypt-protests",itemOne.getLink());
		Assert.assertEquals("2 days ago ... A deadline looms Tuesday for Egyptian President Mohamed Morsy, and another   hangs over his head for Wednesday.",itemOne.getSnippet());
		Assert.assertEquals("Tick tock: Deadlines loom for Egypt's Morsy - CNN.com",itemOne.getTitle());
	}
}
