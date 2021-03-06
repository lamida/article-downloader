package net.lamida.nd.pdf;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDestination;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfOutline;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfJoiner implements IPdfJoiner {
	private Log log = LogFactory.getLog(this.getClass().toString());
	
	/* (non-Javadoc)
	 * @see net.lamida.nd.pdf.IJoinPdf#joinPdf(java.util.List, java.lang.String)
	 */
//	public void joinPdf(List<String> fileInput, String mergedPdfFileName){
//		log.info("joinPdf");
//		try {
//		    List<InputStream> pdfs = new ArrayList<InputStream>();  
//			for(String fileName : fileInput){
//				File file = new File(fileName);
//				pdfs.add(new FileInputStream(file));
//			}
//			OutputStream output = new FileOutputStream(new File(mergedPdfFileName));
//			concatPDFs(pdfs, output, true);
//		} catch (FileNotFoundException e) {
//			log.error(e.getMessage());
//		}
//	}
	
	private List<String> fileName;

	public void joinPdf(String inputDir, String mergedPdfFileName) throws FileNotFoundException{
		log.info("joinPdf");
		File dir = new File(inputDir);
		if(!dir.isDirectory()){
			throw new IllegalArgumentException("inputDir must be directory");
		}
		List<InputStream> pdfs = new ArrayList<InputStream>(); 
		fileName = new ArrayList<String>();
		for(File file : dir.listFiles()){
			System.out.println(file.getName());
			pdfs.add(new FileInputStream(file));
			fileName.add(file.getName().substring(0, file.getName().indexOf(".pdf")));
			file.delete(); 
		}
		OutputStream output = new FileOutputStream(new File(mergedPdfFileName));
		concatPDFs(pdfs, output, true);
		for(File file : dir.listFiles()){
			file.delete();
		}
	}
	
	private void concatPDFs(List<InputStream> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {
		log.info("concatPDFs");
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
			PdfOutline root = writer.getRootOutline();
			PdfOutline bookmark = null;
			
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			PdfContentByte cb = writer.getDirectContent(); // Holds the PDF data

			PdfImportedPage page;
			int currentPageNumber = 0;
			int pageOfCurrentReaderPDF = 0;
			Iterator<PdfReader> iteratorPDFReader = readers.iterator();

			// Loop through the PDF files and add to the output.
			int loop = 0;
			while (iteratorPDFReader.hasNext()) {
				PdfReader pdfReader = iteratorPDFReader.next();
				bookmark = new PdfOutline(root, new PdfDestination(PdfDestination.FIT, 0), fileName.get(loop++));
				
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

	private void paginate(int totalPages, BaseFont bf, PdfContentByte cb, int currentPageNumber) {
		log.info("paginate");
		cb.beginText();
		cb.setFontAndSize(bf, 9);
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
		cb.endText();
	}
}
