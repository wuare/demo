package top.wuare.json.convert;

import org.junit.Assert;
import org.junit.Test;

/**
 * test convert
 *
 * @author wuare
 * @date 2021/6/18
 */
public class JsonConvertTest {

    @Test
    public void testConvertBean() {
        String text = "{ \"id\": 11, \"name\": \"North\" }";
        JsonConvert convert = new JsonConvert();
        UserTest userTest = convert.convertBean(text, UserTest.class);
        Assert.assertEquals(11, userTest.getId());
        Assert.assertEquals("North", userTest.getName());
    }

    @Test
    public void testConvertBean1() {
        String text = "{ \"id\": 11, \"name\": \"North\", \"idCard\": { \"address\": \"Beijing\" } }";
        JsonConvert convert = new JsonConvert();
        UserTest userTest = convert.convertBean(text, UserTest.class);
        Assert.assertEquals(11, userTest.getId());
        Assert.assertEquals("North", userTest.getName());
        Assert.assertNotNull(userTest.getIdCard());
        Assert.assertEquals("Beijing", userTest.getIdCard().getAddress());
    }
}
