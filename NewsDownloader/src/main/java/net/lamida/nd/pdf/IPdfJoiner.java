package net.lamida.nd.pdf;

import java.util.List;

public interface IPdfJoiner {
	void joinPdf(List<String> fileInput, String mergedPdfFileName);
	void joinPdf(String inputDir, String mergedPdfFileName);
}