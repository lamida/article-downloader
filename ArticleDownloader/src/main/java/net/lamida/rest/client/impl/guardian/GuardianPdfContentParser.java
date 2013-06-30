package net.lamida.rest.client.impl.guardian;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lamida.rest.Job;
import net.lamida.rest.RestResult;
import net.lamida.rest.client.IContentParser;
import net.lamida.util.Utils;

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

public class GuardianPdfContentParser extends GuardianJsoupContentParser implements
		IContentParser {
	private Logger log = Logger.getLogger(this.getClass().toString());

	Font highlightFont = new Font(FontFamily.HELVETICA, -1, Font.BOLD, BaseColor.RED);
	
	public GuardianPdfContentParser(Job job, String selector) {
		super(job, selector);
		log.info("construct jobId: " + job.getId());
	}

	public void saveAll() {
		log.info("saveAll jobId: " + job.getId());
		File downloadFolder = new File(Utils.buildDownloadFolder(job.getId()));
		File targetFolder = new File(Utils.buildPdfFolder(job.getId()));
		targetFolder.mkdir();
		
		int i = 0;
		for (File sourceFile : downloadFolder.listFiles()) {
			if (sourceFile.getName().equals(Utils.REST_RESPONSE_RESULT_METADATA_FILE)) {
				continue;
			}
			log.info("sourceFile: " + sourceFile.getName());
			log.info("targetFile: " + sourceFile.getName().replace(Utils.HTML_EXTENSION, Utils.PDF_EXTENSION));
			String content = parse(sourceFile);
			File destinationFile = new File(targetFolder.getAbsolutePath() + File.separator + sourceFile.getName().replace(Utils.HTML_EXTENSION, Utils.PDF_EXTENSION));
			String header = createHeader(job.getResponse().getResults().get(i), content);
			writePdf(destinationFile, job.getResponse().getResults().get(i), header, content);
			i++;
		}
	}

	private void writePdf(File targetFile, RestResult result, String header, String content) {
		log.info("writePdf");
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(targetFile.getAbsolutePath())).setInitialLeading(16);
			document.open();
			document.addTitle(result.getWebTitle());
			document.add(new Chunk(header));
			
			writeContent(document, content, job.getParam().isHighlightKeyWords());
			
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
		Pattern pattern = preparePattern(job.getParam().getQuery().split(" "));
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
	
	public void joinPdf(){
		try {
		    List<InputStream> pdfs = new ArrayList<InputStream>();  
			File pdfFolder = new File(Utils.buildPdfFolder(job.getId()));
			for(File file : pdfFolder.listFiles()){
				if(!file.getName().equals(Utils.MERGED_PDF_FILE)){
					pdfs.add(new FileInputStream(file));
				}
			}
			OutputStream output = new FileOutputStream(new File(pdfFolder, Utils.MERGED_PDF_FILE));
			concatPDFs(pdfs, output, true);
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
	}
	
	public static void concatPDFs(List<InputStream> streamOfPDFFiles,
			OutputStream outputStream, boolean paginate) {

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
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA,
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
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
					page = writer.getImportedPage(pdfReader,
							pageOfCurrentReaderPDF);
					cb.addTemplate(page, 0, 0);

					// Code for pagination.
					if (paginate) {
						cb.beginText();
						cb.setFontAndSize(bf, 9);
						cb.showTextAligned(PdfContentByte.ALIGN_CENTER, ""
								+ currentPageNumber + " of " + totalPages, 520,
								5, 0);
						cb.endText();
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

}
