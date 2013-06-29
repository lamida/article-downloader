package net.lamida.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import junit.framework.Assert;
import net.lamida.rest.client.IContentParser;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.rest.client.IRestResponseFetcher;
import net.lamida.rest.client.impl.guardian.GuardianDefaultDocumentDownloader;
import net.lamida.rest.client.impl.guardian.GuardianJsoupContentParser;
import net.lamida.rest.client.impl.guardian.GuardianPdfContentParser;
import net.lamida.rest.client.impl.guardian.GuardianResponseBuilder;
import net.lamida.rest.client.impl.guardian.GuardianResponseFetcher;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class GuardianRestClientTest {
	
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
		
		Parameter param = new Parameter();
		param.setEndPoint(endPoint);
		param.setApiKey(apiKey);
		param.setQuery(query);
		param.setFormat(format);
		param.setPageSize(pageSize);
		param.setCountKeyWords(true);
		job = new Job(param, "20121125_152413");
		//job = new Job(param);
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
	// worked to avoid too many api call
	public void testResponseFetcher() throws IOException{
		System.out.println("testResponseFetcher");
		URL url = GuardianRestClientTest.class.getResource("/responseSample.json");
		IRestResponseFetcher responseFetcher = new GuardianResponseFetcher(job);
		String actual = responseFetcher.getResponse();
		Assert.assertNotNull(actual);
	}
	
	@Test 
	public void buildResponse() throws IOException{
		File url = new File("result/" + job.getId() + "/html/00_DOWNLOAD_METADATA.json");
		String restResponse = FileUtils.readFileToString(url);
		Assert.assertNotNull(restResponse);
		Response responseData = new GuardianResponseBuilder(job).buildFromLocal(restResponse);
		Assert.assertNotNull(responseData);
	}
	
	@Test
	public void testDownloadArticles() throws IOException{
		System.out.println("testDownloadArticles");
		System.out.println("count words? " + job.getParam().isCountKeyWords());
		File url = new File("result/" + job.getId() + "/html/00_DOWNLOAD_METADATA.json");
		IDocumentDownloader downloader = new GuardianDefaultDocumentDownloader(job, null);
		String restResponse = FileUtils.readFileToString(url);
		Response responseData = new GuardianResponseBuilder(job).buildFromLocal(restResponse);
		downloader.download(responseData);
	}
	
	
	@Test
	public void testJsoupSaveArticles() throws IOException{
		System.out.println("testJsoupSaveArticles");
		File url = new File("result/" + job.getId() + "/html/00_DOWNLOAD_METADATA.json");
		String restResponse = FileUtils.readFileToString(url);
		Response responseData = new GuardianResponseBuilder(job).buildFromLocal(restResponse);
		IContentParser contentParser = new GuardianJsoupContentParser(job, "div#content p");
		contentParser.saveAll();
	}

	@Test
	public void testPdfSaveArticles() throws IOException{
		System.out.println("testPdfSaveArticles");
		File url = new File("result/" + job.getId() + "/html/00_DOWNLOAD_METADATA.json");
		String restResponse = FileUtils.readFileToString(url);
		Response responseData = new GuardianResponseBuilder(job).buildFromLocal(restResponse);
		IContentParser contentParser = new GuardianPdfContentParser(job, "div#content p");
		contentParser.saveAll();
		
		((GuardianPdfContentParser)contentParser).joinPdf();
	}
	
	

}
