package net.lamida.client.facade;

import net.lamida.http.client.facade.CnaFacade;
import net.lamida.rest.client.facade.GuardianFacade;

/**
 * TODO check this
 * @author lamida
 *
 */
public class NewsEngine {
	enum NewsProvider{
		GUARDIAN, CNA;
	}
	
	public static INewsFacade getEngine(NewsProvider provider){
		INewsFacade ret = null;
		switch (provider) {
			case GUARDIAN:
				ret = new GuardianFacade();
				break;
			case CNA:
				ret = new CnaFacade();
				break;
	
			default:
				break;
		}
		return ret;
	}
}
