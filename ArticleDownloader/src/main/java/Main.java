//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import net.lamida.rest.Job;
//import net.lamida.rest.SearchParameter;
//import net.lamida.rest.SearchResponse;
//import net.lamida.rest.client.IContentParser;
//import net.lamida.rest.client.IDocumentDownloader;
//import net.lamida.rest.client.ISearchBuilder;
//import net.lamida.rest.client.impl.DefaultDocumentDownloader;
//import net.lamida.rest.client.impl.guardian.GuardianJsoupContentParser;
//import net.lamida.rest.client.impl.guardian.GuardianPdfContentConverter;
//import net.lamida.rest.client.impl.guardian.GuardianResponseBuilder;
//import net.lamida.rest.client.impl.guardian.GuardianSearchBuilder;
//
//
//public class Main {
//	
//	public static void main4(String[] args) {
//		String template = "\\b{key}\\b";
//		System.out.println(template.replace("{key}", "jon")); 
//	}
//
//	public static void maind(String[] args) throws Exception {
//		Pattern pattern = Pattern.compile("\\bjon\\b|\\blamida\\b");
//		String[] doc = new String[]{"lolo jOn kArtago lamida Jon lamida jon joN", "LAMIDA tutu JON jon"};
//		List<String> modifiedString = new ArrayList<String>();
//		for(String p : doc){
//			System.out.println(p);
//			Matcher m = pattern.matcher(p.toLowerCase());
//			
//			int cursor = 0;
//			while(m.find()){
//				m.group();
//				System.out.println(m.start() + " to " + m.end());
//				modifiedString.add(p.substring(cursor, m.start()));
//				modifiedString.add(p.substring(m.start(), m.end()));
//				cursor = m.end();
//			}
//			modifiedString.add("\n");
//			
//		}
//		for(String s : modifiedString)
//		System.out.print(s);
//	}	
//	
//	public static void main(String[] args) throws Exception {
//		Properties properties = new Properties();
//		properties.load(new FileInputStream(new File("config.properties")));
//		String endPoint = properties.getProperty("guardian.article.api_endpoint");
//		String apiKey = properties.getProperty("guardian.article.api_key");
//		String format = properties.getProperty("guardian.article.api_response_format");
//
//		String query = "cloud platform";
//		String pageSize = "5";
//		SearchParameter param = new SearchParameter();
//		param.setEndPoint(endPoint);
//		param.setApiKey(apiKey);
//		param.setQuery(query);
//		param.setFormat(format);
//		param.setPageSize(pageSize);
//		param.setCountKeyWords(true);
//		param.setHighlightKeyWords(true);
//		
//		Job job = new Job(param);
//		
//		ISearchBuilder responseFetcher = new GuardianSearchBuilder(job);
//		String restResponse = responseFetcher.getSearchResponse();
//		
//		SearchResponse responseData = new GuardianResponseBuilder(job).buildSearchResponse(restResponse);
//		
//		IDocumentDownloader downloader = new DefaultDocumentDownloader(job, null);
//		downloader.download(responseData);
//		
//		IContentParser parser = new GuardianJsoupContentParser(job, "div#content p");
//		parser.saveAll();
//		
//		IContentParser pdfParser = new GuardianPdfContentConverter(job, "div#content p");
//		pdfParser.saveAll();
//		
//		((GuardianPdfContentConverter)pdfParser).joinPdf();
//	}
//}
