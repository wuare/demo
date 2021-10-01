## 说明
json解析库，使用Java编写  
实现较为简单，为了学习词法分析和语法分析
## 使用
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
