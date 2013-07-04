package net.lamida.nd.pdf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class NewsPdfWriter {
	private Logger log = Logger.getLogger(this.getClass().toString());
	Font highlightFont = new Font(FontFamily.HELVETICA, -1, Font.BOLD, BaseColor.RED);
	
	private String searchQuery;
	private String url;
	private String newsTitle;
	private String newsContent;
	private String newsPostTime;
	private boolean countKeyWords;
	private boolean highlightQuery;
	
	public NewsPdfWriter(String searchQuery, String url, String newsTitle,
			String newsContent, String newsPostTime, boolean countKeyWords,
			boolean highlightQuery) {
		super();
		this.searchQuery = searchQuery;
		this.url = url;
		this.newsTitle = newsTitle;
		this.newsContent = newsContent;
		this.newsPostTime = newsPostTime;
		this.countKeyWords = countKeyWords;
		this.highlightQuery = highlightQuery;
	}

	protected String createHeader(){
		log.info("createHeader countKeyWords: " + countKeyWords);
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
		
		StringBuffer sb = new StringBuffer();
		sb.append("Document URL: ");
		sb.append(url);
		sb.append("\n");
		sb.append("Document Title: ");
		sb.append(newsTitle);
		sb.append("\n");
		sb.append("Publication Date: ");
		sb.append(newsPostTime);
		sb.append("\n");
		sb.append("Date of Retrieval: ");
		sb.append(format.format(new Date()));
		sb.append("\n");
		sb.append("Keyword Statistic(s): ");
		sb.append("\n");
		if(countKeyWords){
			Map<String, Integer> counts = calculateKeyword();
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
	
	protected Map<String, Integer> calculateKeyword(){
		log.info("calculateKeywords: document: " + newsContent.substring(0, 100));
		Map<String, Integer> counts = new HashMap<String, Integer>();
		String[] keywords = searchQuery.split(" ");
		for(String keyword: keywords){
			int wordCount = countWord(keyword, newsContent);
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

	public void writePdf(File targetFile) {
		log.info("writePdf");
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(targetFile.getAbsolutePath())).setInitialLeading(16);
			document.open();
			document.addTitle(newsTitle);
			document.add(new Chunk(createHeader()));
			
			writeContent(document, newsContent, highlightQuery);
			
			document.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		} catch (DocumentException e) {
			log.error(e.getMessage());
		}
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
		Pattern pattern = preparePattern(searchQuery.split(" "));
		Matcher m = pattern.matcher(content.toLowerCase());
		int cursor = 0;
		while(m.find()){
			m.group();
			String standardText = content.substring(cursor, m.start());
			String highlightText = content.substring(m.start(), m.end());
			Chunk standard = new Chunk(standardText);
			Chunk highlight = new Chunk(highlightText, highlightFont);
			Phrase phrase = new Phrase();
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
			//log.info("keyword: " + key);
			sb.append(template.replace("{key}", key));
			sb.append("|");
		}
		String patternText = sb.toString().substring(0, sb.toString().length());
		//log.info("patternText: " + patternText);
		return Pattern.compile(patternText);
	}
	
	public void joinPdf(String pdfFolderName, String mergedPdfFileName){
		try {
		    List<InputStream> pdfs = new ArrayList<InputStream>();  
			File pdfFolder = new File(pdfFolderName);
			for(File file : pdfFolder.listFiles()){
				if(!file.getName().equals(mergedPdfFileName)){
					pdfs.add(new FileInputStream(file));
				}
			}
			OutputStream output = new FileOutputStream(new File(pdfFolder, mergedPdfFileName));
			concatPDFs(pdfs, output, true);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	private static void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

		Document document = new Document();
		try {
			List<InputStream> pdfs = streamOfPDFFiles;
			List<PdfReader> readers = new ArrayList<PdfReader>();
			int totalPages = 0;
			Iterator<InputStream> iteratorPDFs = pdfs.iterator();

			// Create Readers for the pdfs.
			while (iteratorPDFs.hasNext()) {
				InputStream pdf = iteratorPDFs.next();
				PdfReader pdfReader = new PdfReader(pdf);
				readers.add(pdfReader);
				totalPages += pdfReader.getNumberOfPages();
			}
			// Create a writer for the outputstream
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);

			document.open();
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
			// data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();

				// Create a new page in the target for each source page.
				while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
					document.newPage();
					pageOfCurrentReaderPDF++;
					currentPageNumber++;
					page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					if (paginate) {
						paginate(totalPages, bf, cb, currentPageNumber);
					}
				}
				pageOfCurrentReaderPDF = 0;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	private static void paginate(int totalPages, BaseFont bf, PdfContentByte cb, int currentPageNumber) {
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
				+ currentPageNumber + " of " + totalPages, 520,
				5, 0);
		cb.endText();
	}
}
