package net.lamida.nd.pdf;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class PdfWriterTest {

	@Test
	public void testWrite() throws IOException {
		String searchQuery = "egypt";
		String url = "foo_bar";
		String newsContent = FileUtils.readFileToString(new File("textSample"));
		String newsTitle = "Foo bar";
		String newsPostTime = "Foo bar";
		PdfInputData input = new PdfInputData(searchQuery, url, newsTitle,
				newsContent, newsPostTime, 10);

		INewsPdfWriter writer = new NewsPdfWriter();
		writer.init(input, "my3.pdf", true, true, 10);
		writer.writePdf();

	}
}
