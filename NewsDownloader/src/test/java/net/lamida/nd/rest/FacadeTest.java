//package net.lamida.nd.rest;
//
//import java.io.File;
//import java.io.IOException;
//
//import junit.framework.Assert;
//import net.lamida.nd.Utils;
//import net.lamida.nd.bean.SearchResult;
//import net.lamida.nd.bean.SearchResultItem;
//import net.lamida.nd.parser.AbstractParser;
//import net.lamida.nd.parser.IParser;
//
//import org.apache.commons.io.FileUtils;
//import org.easymock.EasyMock;
//import org.junit.Test;
//
//public class FacadeTest {
//	//@Test
//	public void endToEnd() throws Exception{
//		String query = "foo bar";
//		SearchProviderEnum provider = SearchProviderEnum.CNN;
//		//IRestSearch search = AbstractRestSearch.getSearchProvider(provider);
//		IRestSearch mock = EasyMock.createNiceMock(IRestSearch.class);
//		String dummyRest = Utils.readFileToString(new File("googleSearchCnn.txt"));
//		Assert.assertNotNull(dummyRest);
//		EasyMock.expect(mock.execute()).andReturn(dummyRest);
//		EasyMock.replay(mock);
//		String result = mock.execute();
//		Assert.assertNotNull(result);
//		
//		SearchResultBuilder builder = new SearchResultBuilder();
//		SearchResult build = builder.build(result);
//		Assert.assertNotNull(build);
//		
//		for(SearchResultItem item: build.getItems()){
//			System.out.println("processing " + item.getTitle());
//			IParser parser = AbstractParser.getParser(provider);
//			parser.init(item.getLink());
//			FileUtils.writeStringToFile(new File("result", item.getTitle() + ".txt"), parser.getNewsContent());
//		}
//	}
//	
//	
////	@Test
////	public void foo(){
////		AbstractFacade facade = new AbstractFacade();
////		facade.setSearchProvider(new CnnRestSearch());
////		facade.setQuery("");
////	}
//}
