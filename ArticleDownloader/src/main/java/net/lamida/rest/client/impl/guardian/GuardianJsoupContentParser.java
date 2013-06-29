package net.lamida.rest.client.impl.guardian;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lamida.rest.Job;
import net.lamida.rest.Result;
import net.lamida.rest.client.IContentParser;
import net.lamida.util.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GuardianJsoupContentParser implements IContentParser {
	Logger log = Logger.getLogger(this.getClass().toString());
	protected String selector;
	protected Job job;
	public static final String PARSED_FOLDER = "txt";
	
	public GuardianJsoupContentParser(Job job, String selector) {
		log.info("construct jobId: " + job.getId());
		this.job = job;
		this.selector = selector;
	}
	
	public void saveAll(){
		log.info("saveAll jobId: " + job.getId());
		File targetFolder = new File(Job.RESULT_DIRECTORY + File.separator + job.getId() + File.separator + GuardianJsoupContentParser.PARSED_FOLDER);
		File downloadFolder = new File(Job.RESULT_DIRECTORY + File.separator + job.getId() + File.separator + GuardianDefaultDocumentDownloader.DOWNLOAD_FOLDER);
		
		int i = 0;
		try {
			for(File sourceFile : downloadFolder.listFiles()){
				if(sourceFile.getName().equals(GuardianDefaultDocumentDownloader.RESULT_METADATA_FILE)){
					continue;
				}
				log.info("sourceFile: " + sourceFile.getName());
				log.info("targetFile: " + sourceFile.getName().replace(Utils.HTML_EXTENSION, Utils.TXT_EXTENSION));
				String parsedDocument = parse(sourceFile);
				File destinationFile = new File(targetFolder.getAbsolutePath() + File.separator + sourceFile.getName().replace(Utils.HTML_EXTENSION, Utils.TXT_EXTENSION));
				String header = createHeader(job.getResponse().getResults().get(i), parsedDocument);
				FileUtils.writeStringToFile(destinationFile, header + parsedDocument);
				i++;
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public String parse(File htmlFile) {
		StringBuffer sb = new StringBuffer();
		try {
			Document doc = Jsoup.parse(htmlFile, Charset.defaultCharset().name());
			Elements paragraphs = doc.select(selector);
			for(Element el : paragraphs){
				String p = el.text();
				sb.append(p);
				sb.append("\n\n");
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return sb.toString();
	}
	
	protected String createHeader(Result res, String parsedDocument){
		log.info("createHeader countKeyWords: " + job.getParam().isCountKeyWords());
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
		
		StringBuffer sb = new StringBuffer();
		sb.append("Document URL: ");
		sb.append(res.getWebUrl());
		sb.append("\n");
		sb.append("Document Title: ");
		sb.append(res.getWebTitle());
		sb.append("\n");
		sb.append("Publication Date: ");
		sb.append(res.getWebPublicationDate());
		sb.append("\n");
		sb.append("Date of Retrieval: ");
		sb.append(format.format(new Date()));
		sb.append("\n");
		sb.append("Keyword Statistic(s): ");
		sb.append("\n");
		if(job.getParam().isCountKeyWords()){
			Map<String, Integer> counts = calculateKeyword(parsedDocument);
			for(String key : counts.keySet()){
				sb.append(key);
				sb.append(": ");
				sb.append(counts.get(key));
				sb.append(" occurences ");
				sb.append("\n");
			}
		}
		
		sb.append("==========================================================================");
		sb.append("\n");
		sb.append("\n");
		
		return sb.toString();
	}
	
	protected Map<String, Integer> calculateKeyword(String documentContent){
		log.info("calculateKeywords: document: " + documentContent.substring(0, 100));
		Map<String, Integer> counts = new HashMap<String, Integer>();
		String[] keywords = job.getParam().getQuery().split(" ");
		for(String keyword: keywords){
			int wordCount = countWord(keyword, documentContent);
			//log.info("calculate Keyword: " + keyword + " count: " + wordCount);
			counts.put(keyword, wordCount);
		}
		return counts;
	}
	
	protected int countWord(String keyword, String document){
		log.info("countWord");
		String patternText = "\\b{key}\\b";
		Pattern pattern = Pattern.compile(patternText.replace("{key}", keyword.toLowerCase()));
		Matcher m = pattern.matcher(document.toLowerCase());
		int count = 0;
		while(m.find()){
			count++;
		}
		return count;
	}
	

}
