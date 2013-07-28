package net.lamida.http.client.facade;

import net.lamida.client.facade.INewsFacade;
import net.lamida.http.client.impl.cna.CnaJsoupContentParser;
import net.lamida.http.client.impl.cna.CnaPdfContentConverter;
import net.lamida.http.client.impl.cna.CnaSearchBuilder;
import net.lamida.rest.Job;
import net.lamida.rest.SearchResponse;
import net.lamida.rest.client.IContentParser;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.rest.client.ISearchBuilder;
import net.lamida.rest.client.impl.DefaultDocumentDownloader;
import net.lamida.rest.client.impl.guardian.GuardianPdfContentConverter;
import net.lamida.util.ProgressReporter;

public class CnaFacade implements INewsFacade{
	public void process(Job job, ProgressReporter progressReporter) {
		SearchResponse response = fetchResult(job);
		download(job, response, progressReporter);
	}
	
	public SearchResponse fetchResult(Job job){
		ISearchBuilder searchBuilder = new CnaSearchBuilder(job);
		return searchBuilder.buildSearchResponse();
	}
	
	public void download(Job job, SearchResponse responseData, ProgressReporter progressReporter){
		IDocumentDownloader downloader = new DefaultDocumentDownloader(job, progressReporter);
		downloader.download(responseData);
		
		IContentParser parser = new CnaJsoupContentParser(job, "div.news_detail p");
		parser.saveAll();
		
		IContentParser pdfParser = new CnaPdfContentConverter(job, "div.news_detail p");
		pdfParser.saveAll();
		
		((GuardianPdfContentConverter)pdfParser).joinPdf();
	}
}
