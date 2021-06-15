## Json Parser
Usage: 
```java
package top.wuare.json.parser;

/**
 * test JsonParser
 */
public class JsonParserTest {

    public static void main(String[] args) {
        String text = "{ \"name\": \"alis\", \"age\": 18, \"height\": 1.69 }";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(text);
        System.out.println(obj);
    }
}
```