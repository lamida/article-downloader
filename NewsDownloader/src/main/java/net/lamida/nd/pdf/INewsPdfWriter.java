package net.lamida.nd.pdf;

public interface INewsPdfWriter {

	public abstract void init(PdfInputData data, String targetFileName, boolean countKeyWords,boolean highlightQuery, boolean downloadImage, int totalArticleCount);
	public abstract void writePdf();

}