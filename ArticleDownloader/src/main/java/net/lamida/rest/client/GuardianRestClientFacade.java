package net.lamida.rest.client;
import net.lamida.rest.Job;
import net.lamida.rest.RestResponse;
import net.lamida.rest.client.impl.guardian.GuardianDefaultDocumentDownloader;
import net.lamida.rest.client.impl.guardian.GuardianJsoupContentParser;
import net.lamida.rest.client.impl.guardian.GuardianPdfContentParser;
import net.lamida.rest.client.impl.guardian.GuardianResponseBuilder;
import net.lamida.rest.client.impl.guardian.GuardianResponseFetcher;
import net.lamida.util.ProgressReporter;


public class GuardianRestClientFacade {
	public static RestResponse fetchResult(Job job){
		IRestResponseFetcher responseFetcher = new GuardianResponseFetcher(job);
		String restResponse = responseFetcher.getResponse();
		
		return new GuardianResponseBuilder(job).buildFromServer(restResponse);
	}
	
	public static void download(Job job, RestResponse responseData, ProgressReporter progressReporter){
		IDocumentDownloader downloader = new GuardianDefaultDocumentDownloader(job, progressReporter);
		downloader.download(responseData);
		
		IContentParser parser = new GuardianJsoupContentParser(job, "div#content p");
		parser.saveAll();
		
		IContentParser pdfParser = new GuardianPdfContentParser(job, "div#content p");
		pdfParser.saveAll();
		
		((GuardianPdfContentParser)pdfParser).joinPdf();
	}
}
