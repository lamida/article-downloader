import junit.framework.Assert;
import net.lamida.nd.Utils;

import org.junit.Test;



public class UtilsTest {

	@Test
	public void storeConfigTest(){
		Utils.saveConfiguration("aljazeera-key", "foo bar");
		String key = Utils.loadConfiguration("aljazeera-key");
		//Assert.assertNotNull(key); 
		Assert.assertEquals("foo bar", key);
	}
}
