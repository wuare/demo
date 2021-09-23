## Json Parser
Usage:

```java
package top.wuare.json;

/**
 * test Wson
 */
public class WsonTest {

    public static void main(String[] args) {
        String text = "{ \"name\": \"alis\", \"age\": 18, \"height\": 1.69 }";
        Wson wson = new Wson();
        // 该方法返回Map，List，Boolean，String，BigDecimal，null其中的一种
        // 对应JSON的各种类型
        Object obj = wson.fromJson(text);
        System.out.println(obj);

        // 或者指定Class
        // 实体类中必须指定get set方法
        // 且目前不支持List，Map的反序列化，使用数组代替List
        User user = wson.fromJson(text, User.class);
        System.out.println(user);
    }

    public static class User {
        private String name;
        private int age;
        private double height;
        // get set 方法...
    }
}
```