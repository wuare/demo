package top.wuare.json.parser;

import org.junit.Test;
import top.wuare.json.exception.CommonException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * test JsonParser
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonParserTest {

    @Test
    public void testParser() {
        String text = "{ \"orderCode\": \"C001\", \"type\": 1, \"pageNo\": 3 , \"amount\": -1.236 }";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(text);
        System.out.println(obj);
    }

    @Test
    public void testParser0() throws Exception {
        File file = new File("Your Json File Path!!!");
        if (!file.exists()) {
            return;
        }
        if (!file.isFile()) {
            return;
        }
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {}
        }

        String t = out.toString();
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(t);
        System.out.println(obj);
    }

    @Test
    public void testParser1() {
        String t = "{\n" +
                "    \"transactionInfo\":{\n" +
                "        \"transactionId\":\"\",\n" +
                "        \"transactionType\":\"\",\n" +
                "        \"transactionExeDate\":\"\",\n" +
                "        \"transactionExeTime\":\"\"\n" +
                "    },\n" +
                "    \"channelInfo\":{\n" +
                "        \"sellingChannelCode\":\"\",\n" +
                "        \"clientSystemId\":\"\",\n" +
                "        \"agency\":\"\",\n" +
                "        \"agent\":\"\",\n" +
                "        \"customerManager\":\"\"\n" +
                "    },\n" +
                "    \"response\":{\n" +
                "        \"status\":{\n" +
                "            \"statusCode\":\"400\",\n" +
                "            \"statusMessage\":[\n" +
                "\n" +
                "            ]\n" +
                "        },\n" +
                "        \"quote\":{\n" +
                "            \"amnt\":\"\",\n" +
                "            \"prem\":\"\",\n" +
                "            \"effectiveDate\":\"2015-01-01\",\n" +
                "            \"expirationDate\":\"2016-01-01\",\n" +
                "            \"components\":[\n" +
                "                {\n" +
                "                    \"riskCode\":\"RA001\",\n" +
                "                    \"amnt\":\"100000\",\n" +
                "                    \"prem\":\"10\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"riskCode\":\"RA002\",\n" +
                "                    \"amnt\":\"200000\",\n" +
                "                    \"prem\":\"20\"\n" +
                "                }\n" +
                "            ]\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(t);
        System.out.println(obj);
    }

    // https://github.com/antlr/grammars-v4/blob/master/json/examples/example1.json
    @Test
    public void testParser2() {
        String t = "{\n" +
                "    \"glossary\": {\n" +
                "        \"title\": \"example glossary\",\n" +
                "\t\t\"GlossDiv\": {\n" +
                "            \"title\": \"S\",\n" +
                "\t\t\t\"GlossList\": {\n" +
                "                \"GlossEntry\": {\n" +
                "                    \"ID\": \"SGML\",\n" +
                "\t\t\t\t\t\"SortAs\": \"SGML\",\n" +
                "\t\t\t\t\t\"GlossTerm\": \"Standard Generalized Markup Language\",\n" +
                "\t\t\t\t\t\"Acronym\": \"SGML\",\n" +
                "\t\t\t\t\t\"Abbrev\": \"ISO 8879:1986\",\n" +
                "\t\t\t\t\t\"GlossDef\": {\n" +
                "                        \"para\": \"A meta-markup language, used to create markup languages such as DocBook.\",\n" +
                "\t\t\t\t\t\t\"GlossSeeAlso\": [\"GML\", \"XML\"]\n" +
                "                    },\n" +
                "\t\t\t\t\t\"GlossSee\": \"markup\"\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(t);
        System.out.println(obj);
    }

    @Test(expected = CommonException.class)
    public void testTxtEnd() {
        String t = "1 2 ";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(t);
        System.out.println(obj);
    }
}
