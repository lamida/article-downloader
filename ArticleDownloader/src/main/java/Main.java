import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lamida.rest.Job;
import net.lamida.rest.RestParameter;
import net.lamida.rest.RestResponse;
import net.lamida.rest.client.IContentParser;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.rest.client.IRestResponseFetcher;
import net.lamida.rest.client.impl.guardian.GuardianDefaultDocumentDownloader;
import net.lamida.rest.client.impl.guardian.GuardianJsoupContentParser;
import net.lamida.rest.client.impl.guardian.GuardianPdfContentParser;
import net.lamida.rest.client.impl.guardian.GuardianResponseBuilder;
import net.lamida.rest.client.impl.guardian.GuardianResponseFetcher;


public class Main {
	
	public static void main4(String[] args) {
		String template = "\\b{key}\\b";
		System.out.println(template.replace("{key}", "jon")); 
	}

	public static void maind(String[] args) throws Exception {
		Pattern pattern = Pattern.compile("\\bjon\\b|\\blamida\\b");
		String[] doc = new String[]{"lolo jOn kArtago lamida Jon lamida jon joN", "LAMIDA tutu JON jon"};
		List<String> modifiedString = new ArrayList<String>();
		for(String p : doc){
			System.out.println(p);
			Matcher m = pattern.matcher(p.toLowerCase());
			
			int cursor = 0;
			while(m.find()){
				m.group();
				System.out.println(m.start() + " to " + m.end());
				modifiedString.add(p.substring(cursor, m.start()));
				modifiedString.add(p.substring(m.start(), m.end()));
				cursor = m.end();
			}
			modifiedString.add("\n");
			
		}
		for(String s : modifiedString)
		System.out.print(s);
	}	
	
	public static void main(String[] args) throws Exception {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("config.properties")));
		String endPoint = properties.getProperty("guardian.article.api_endpoint");
		String apiKey = properties.getProperty("guardian.article.api_key");
		String format = properties.getProperty("guardian.article.api_response_format");

		String query = "cloud platform";
		String pageSize = "5";
		RestParameter param = new RestParameter();
		param.setEndPoint(endPoint);
		param.setApiKey(apiKey);
		param.setQuery(query);
		param.setFormat(format);
		param.setPageSize(pageSize);
		param.setCountKeyWords(true);
		param.setHighlightKeyWords(true);
		
		Job job = new Job(param);
		
		IRestResponseFetcher responseFetcher = new GuardianResponseFetcher(job);
		String restResponse = responseFetcher.getResponse();
		
		RestResponse responseData = new GuardianResponseBuilder(job).buildFromServer(restResponse);
		
		IDocumentDownloader downloader = new GuardianDefaultDocumentDownloader(job, null);
		downloader.download(responseData);
		
		IContentParser parser = new GuardianJsoupContentParser(job, "div#content p");
		parser.saveAll();
		
		IContentParser pdfParser = new GuardianPdfContentParser(job, "div#content p");
		pdfParser.saveAll();
		
		((GuardianPdfContentParser)pdfParser).joinPdf();
	}
}
