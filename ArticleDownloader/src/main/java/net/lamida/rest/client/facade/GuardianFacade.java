package net.lamida.rest.client.facade;

import net.lamida.client.facade.INewsFacade;
import net.lamida.rest.Job;
import net.lamida.rest.SearchResponse;
import net.lamida.rest.client.IContentParser;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.rest.client.ISearchBuilder;
import net.lamida.rest.client.impl.DefaultDocumentDownloader;
import net.lamida.rest.client.impl.guardian.GuardianJsoupContentParser;
import net.lamida.rest.client.impl.guardian.GuardianPdfContentConverter;
import net.lamida.rest.client.impl.guardian.GuardianSearchBuilder;
import net.lamida.util.ProgressReporter;

public class GuardianFacade implements INewsFacade{
	public void process(Job job, ProgressReporter progressReporter) {
		SearchResponse response = fetchResult(job);
		download(job, response, progressReporter);
	}
	
	public SearchResponse fetchResult(Job job){
		ISearchBuilder searchBuilder = new GuardianSearchBuilder(job);
		return searchBuilder.buildSearchResponse();
	}
	
	public void download(Job job, SearchResponse responseData, ProgressReporter progressReporter){
		IDocumentDownloader downloader = new DefaultDocumentDownloader(job, progressReporter);
		downloader.download(responseData);
		
		IContentParser parser = new GuardianJsoupContentParser(job, "div#content p");
		parser.saveAll();
		
		IContentParser pdfParser = new GuardianPdfContentConverter(job, "div#content p");
		pdfParser.saveAll();
		
		((GuardianPdfContentConverter)pdfParser).joinPdf();
	}
}
