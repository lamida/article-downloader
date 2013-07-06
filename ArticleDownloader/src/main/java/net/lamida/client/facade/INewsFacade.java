package net.lamida.client.facade;

import net.lamida.rest.Job;
import net.lamida.util.ProgressReporter;

public interface INewsFacade {
	void process(Job job, ProgressReporter progressReporter);
}	
