package net.lamida.nd.pdf;

import java.io.File;

import org.junit.Test;

public class PdfWriterTest {
	
	@Test
	public void testWrite(){
		String searchQuery = "Egypt";
		String url = "foo_bar";
		String newsContent = "Lorem ipsum";
		String newsTitle = "Foo bar";
		String newsPostTime= "Foo bar";
		NewsPdfWriter writer = new NewsPdfWriter(searchQuery, url, newsTitle, newsContent, newsPostTime, false, false);
		writer.writePdf(new File("new_pdf.pdf"));
		
	}
}
