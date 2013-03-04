package net.lamida.util;

public interface ProgressReporter {
	void updateCurrentProcess(int i);
	void updateCurrentStatus(String message);
}
