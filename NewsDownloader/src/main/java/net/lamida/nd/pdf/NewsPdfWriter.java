package net.lamida.nd.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;

public class NewsPdfWriter implements INewsPdfWriter {
	private Logger log = Logger.getLogger(this.getClass().toString());
	Font highlightFont = new Font(FontFamily.HELVETICA, -1, Font.BOLD, BaseColor.RED);
	
	private PdfInputData data;
	private String targetFileName;
	private boolean countKeyWords;
	private boolean highlightQuery;
	
	public NewsPdfWriter(){}
	
	/* (non-Javadoc)
	 * @see net.lamida.nd.pdf.INewsPdfWriter#init(net.lamida.nd.pdf.NewsPdfWriterData, boolean, boolean)
	 */
	public void init(PdfInputData data, String targetFileName, boolean countKeyWords, boolean highlightQuery) {
		if(targetFileName == null || data == null){
			throw new IllegalArgumentException("OuputFileName or Data cannot be null");
		}
		this.data = data;
		this.targetFileName = targetFileName;
		this.countKeyWords = countKeyWords;
		this.highlightQuery = highlightQuery;
	}
	
	/* (non-Javadoc)
	 * @see net.lamida.nd.pdf.INewsPdfWriter#writePdf(java.lang.String)
	 */
	public void writePdf() {
		if(targetFileName == null || data == null){
			throw new IllegalArgumentException("Call init first");
		}
		
		log.info("writePdf");
		try {
			Document document = new Document();
			File targetFile = new File(targetFileName);
			PdfWriter.getInstance(document, new FileOutputStream(targetFile.getAbsolutePath())).setInitialLeading(16);
			document.open();
			writeHeader(document);
			writeContent(document, data.getNewsContent(), highlightQuery);
			document.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error(e.getMessage());
		}
	}

	private String createHeader(){
		log.info("createHeader countKeyWords: " + countKeyWords);
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
		
		StringBuffer sb = new StringBuffer();
		sb.append("Document URL: ");
		sb.append(data.getUrl());
		sb.append("\n");
		sb.append("Document Title: ");
		sb.append(data.getNewsTitle());
		sb.append("\n");
		sb.append("Publication Date: ");
		sb.append(data.getNewsPostTime());
		sb.append("\n");
		sb.append("Date of Retrieval: ");
		sb.append(format.format(new Date()));
		sb.append("\n");
		sb.append("Keyword Statistic(s): ");
		sb.append("\n");
		if(countKeyWords){
			writeCountKeywords(sb);
		}
		
		sb.append("==========================================================================");
		sb.append("\n");
		sb.append("\n");
		
		return sb.toString();
	}

	private void writeCountKeywords(StringBuffer sb) {
		WordCalculator wordCalc = new WordCalculator();
		Map<String, Integer> counts = wordCalc.calculateKeyword(data.getSearchQuery(), data.getNewsContent());
		for(String key : counts.keySet()){
			sb.append(key);
			sb.append(": ");
			sb.append(counts.get(key));
			sb.append(" occurences ");
			sb.append("\n");
		}
	}
	
	private void writeHeader(Document document) throws DocumentException {
		log.info("writeContent");
		document.addTitle(data.getNewsTitle());
		document.add(new Chunk(createHeader()));
	}
	
	private void writeContent(Document document, String content, boolean highlightQuery) throws DocumentException{
		log.info("writeContent");
		if(highlightQuery){
			highlightText(document, content);
		}else{
			document.add(new Chunk(content));
		}
	}
	
	
	private void highlightText(Document document, String content) throws DocumentException{
		log.info("highlightText");
		Pattern pattern = preparePattern(data.getSearchQuery().split(" "));
		Matcher m = pattern.matcher(content.toLowerCase());
		int cursor = 0;
		while(m.find()){
			m.group();
			String standardText = content.substring(cursor, m.start());
			String highlightText = content.substring(m.start(), m.end());
			Chunk standard = new Chunk(standardText);
			Chunk highlight = new Chunk(highlightText, highlightFont);
			Phrase phrase = new Paragraph();
			phrase.add(standard);
			phrase.add(highlight);
			document.add(phrase);
			cursor = m.end();
		}
	}
	
	private Pattern preparePattern(String[] keywords){
		log.info("preparePattern keys: ");
		String template = "\\b{key}\\b";
		StringBuilder sb = new StringBuilder();
		for(String key: keywords){
			sb.append(template.replace("{key}", key));
			sb.append("|");
		}
		String patternText = sb.toString().substring(0, sb.toString().length());
		if (patternText.length() > 0 && patternText.charAt(patternText.length() - 1) == '|') {
			patternText = patternText.substring(0, patternText.length() - 1);
		}
		return Pattern.compile(patternText);
	}
}
