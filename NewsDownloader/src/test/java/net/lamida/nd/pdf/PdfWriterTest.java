package net.lamida.nd.pdf;

import org.junit.Test;

public class PdfWriterTest {

	@Test
	public void testWrite() {
		String searchQuery = "five";
		String url = "foo_bar";
		String newsContent = "Figure 1.5 already gives you a peek at the source code. The hello.pdf file is created in "
				+ "five steps. The next section discusses every step in detail."
				+ "five steps. The next section discusses every step in detail."
				+ "five steps. The next section discusses every step in detail."
				+ "five steps. The next section discusses every step in detail."
				+ "five steps. The next section discusses every step in detail."
				+ "five steps. The next section discusses every step in detail."
				+ "five steps. The next section discusses every step in detail."
				+ "Figure 1.5 already gives you a peek at the source code. The hello.pdf file is created in five steps. The next section discusses every step in detail.";
		String newsTitle = "Foo bar";
		String newsPostTime = "Foo bar";
		PdfInputData input = new PdfInputData(searchQuery, url, newsTitle,
				newsContent, newsPostTime);

		INewsPdfWriter writer = new NewsPdfWriter();
		writer.init(input, "my3.pdf", true, true);
		writer.writePdf();

	}
}
