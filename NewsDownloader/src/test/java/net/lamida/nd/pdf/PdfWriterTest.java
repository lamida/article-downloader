package net.lamida.nd.pdf;

import org.junit.Test;

public class PdfWriterTest {

//	@Test
//	public void testWrite() throws IOException {
//		String searchQuery = "egypt";
//		String url = "foo_bar";
//		String newsContent = FileUtils.readFileToString(new File("textSample"));
//		String newsTitle = "Foo bar";
//		String newsPostTime = "Foo bar";
//		PdfInputData input = new PdfInputData(searchQuery, url, newsTitle,
//				newsContent, newsPostTime, new NewsImage(), 10, 10);
//
//		INewsPdfWriter writer = new NewsPdfWriter();
//		writer.init(input, "my3.pdf", true, true, true, 10);
//		writer.writePdf();
//
//	}
	
	@Test
	public void testJoin() throws Exception{
		PdfJoiner joiner = new PdfJoiner();
		joiner.joinPdf("joinTest", "mergedFitr.pdf");
	}
}
