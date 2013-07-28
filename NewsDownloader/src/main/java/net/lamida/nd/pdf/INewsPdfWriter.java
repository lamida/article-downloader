package net.lamida.nd.pdf;

public interface INewsPdfWriter {

	public abstract void init(PdfInputData data, String targetFileName, boolean countKeyWords,
			boolean highlightQuery);

	public abstract void writePdf();

}