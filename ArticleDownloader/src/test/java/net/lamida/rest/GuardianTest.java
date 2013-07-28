package net.lamida.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import junit.framework.Assert;
import net.lamida.rest.client.IContentParser;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.rest.client.impl.DefaultDocumentDownloader;
import net.lamida.rest.client.impl.guardian.GuardianJsoupContentParser;
import net.lamida.rest.client.impl.guardian.GuardianPdfContentConverter;
import net.lamida.rest.client.impl.guardian.GuardianSearchBuilder;
import net.lamida.util.Utils;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class GuardianTest {
	
	private Job job;
	
	@Before
	public void init() throws IOException{
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("config.properties")));
		String endPoint = properties.getProperty("guardian.article.api_endpoint");
		String apiKey = properties.getProperty("guardian.article.api_key");
		String format = properties.getProperty("guardian.article.api_response_format");
		String query = "indonesia investment";
		String pageSize = "5";
		
		SearchParameter param = new SearchParameter();
		param.setEndPoint(endPoint);
		param.setApiKey(apiKey);
		param.setQuery(query);
		param.setFormat(format);
		param.setPageSize(pageSize);
		param.setCountKeyWords(true);
		job = new Job(param);
		job.setId("20121125_152413");
		System.out.println("JOB ID: " + job.getId());
		System.out.println("Count Keyword: " + job.getParam().isCountKeyWords());
	}
	
	@Test
	public void initialization(){
		Assert.assertEquals("http://content.guardianapis.com/search", job.getParam().getEndPoint());
		Assert.assertEquals("827p92h8prnwjrnh7z7qr94j", job.getParam().getApiKey());
		Assert.assertEquals("json", job.getParam().getFormat());
	}
	
	
	@Test 
	public void testBuildResponse() throws IOException{
		System.out.println("testBuildResponse");
		File restResponseFile = new File(Utils.buildRestResultMetadataFilePath(job.getId()));
		String restResponse = FileUtils.readFileToString(restResponseFile);
		Assert.assertNotNull(restResponse);
		SearchResponse responseData =  new GuardianSearchBuilder(job).buildSearchResponse();
		Assert.assertNotNull(responseData);
		Assert.assertNotNull(responseData.getResults());
	}
	
	@Test
	public void testDownloadArticles() throws IOException{
		System.out.println("testDownloadArticles");
		System.out.println("count words? " + job.getParam().isCountKeyWords());
		File restResponseFile = new File(Utils.buildRestResultMetadataFilePath(job.getId()));
		IDocumentDownloader downloader = new DefaultDocumentDownloader(job, null);
		String restResponse = FileUtils.readFileToString(restResponseFile);
		Assert.assertNotNull(restResponse);
		SearchResponse responseData = new GuardianSearchBuilder(job).buildSearchResponse();
		Assert.assertNotNull(responseData);
		Assert.assertNotNull(responseData.getResults());
		downloader.download(responseData);
	}
	
	
	//@Test
	public void testJsoupSaveArticles() throws IOException{
		System.out.println("testJsoupSaveArticles");
		IContentParser contentParser = new GuardianJsoupContentParser(job, "div#content p");
		contentParser.saveAll();
	}

	//@Test
	public void testPdfSaveArticles() throws IOException{
		System.out.println("testPdfSaveArticles");
		IContentParser contentParser = new GuardianPdfContentConverter(job, "div#content p");
		contentParser.saveAll();
		
		((GuardianPdfContentConverter)contentParser).joinPdf();
	}
	

}
