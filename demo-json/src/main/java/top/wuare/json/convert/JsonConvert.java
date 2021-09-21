package top.wuare.json.convert;

import top.wuare.json.parser.JsonParser;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * json convert to java object
 *
 * @author wuare
 * @since 2021/6/18
 */
public class JsonConvert {

    private final JsonParser jsonParser = new JsonParser();

    public String toJson(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Integer || obj instanceof Long
                || obj instanceof Float || obj instanceof Double) {
            return obj.toString();
        }
        if (obj instanceof BigDecimal) {
            return ((BigDecimal) obj).toPlainString();
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).toString();
        }
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        }
        if (obj instanceof List) {
            StringBuilder s0 = new StringBuilder("[");
            List<?> list = (List<?>) obj;
            for (Object o : list) {
                s0.append(toJson(o)).append(",");
            }
            if (s0.length() > 1) {
                s0.setLength(s0.length() - 1);
            }
            return s0.append("]").toString();
        }
        if (obj instanceof Map) {
            StringBuilder s0 = new StringBuilder("{");
            Map<Object, Object> map = (Map<Object, Object>) obj;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                s0.append("\"").append(entry.getKey().toString()).append("\"")
                        .append(":").append(toJson(entry.getValue())).append(",");
            }
            if (s0.length() > 1) {
                s0.setLength(s0.length() - 1);
            }
            return s0.append("}").toString();
        }

        Field[] fields = obj.getClass().getDeclaredFields();
        StringBuilder s0 = new StringBuilder("{");
        for (Field f : fields) {
            try {
                PropertyDescriptor descriptor = new PropertyDescriptor(f.getName(), obj.getClass());
                Method readMethod = descriptor.getReadMethod();
                s0.append("\"").append(descriptor.getName()).append("\"")
                        .append(":").append(toJson(readMethod.invoke(obj))).append(",");
            } catch (IntrospectionException | ReflectiveOperationException e) {
                if (e instanceof ReflectiveOperationException) {
                    System.out.println(e.getMessage());
                }
            }
        }
        if (s0.length() > 1) {
            s0.setLength(s0.length() - 1);
        }
        return s0.append("}").toString();
    }
}
