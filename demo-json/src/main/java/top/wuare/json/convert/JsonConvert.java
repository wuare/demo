package top.wuare.json.convert;

import top.wuare.json.exception.CommonException;
import top.wuare.json.lexer.Token;
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

    @SuppressWarnings({"unchecked"})
    public <T> T convertBean(String text, Class<T> t) {
        Object object = jsonParser.parse(text);
        if (object == null) {
            return null;
        }
        if (t == Boolean.class && object instanceof Boolean) {
            return (T) object;
        }
        if (t == String.class && object instanceof String) {
            return (T) object;
        }
        if (object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;
            return mapToBean(t, map);

        }
        throw new CommonException("convert error");
    }

    @SuppressWarnings({"unchecked"})
    private <T> T mapToBean(Class<T> t, Map<String, Object> map) {
        T ins;
        try {
            ins = t.newInstance();
            if (map.size() == 0) {
                return ins;
            }
            Field[] fields = t.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                // TODO maybe Map or List, need handle
                Object o = map.get(name);
                if (o == null) {
                    continue;
                }
                field.setAccessible(true);
                if (o instanceof Map) {
                    field.set(ins, mapToBean(field.getType(), (Map<String, Object>) o));
                    continue;
                }
                fieldSetValue(field, ins, o);
            }
            return ins;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CommonException(e);
        }
    }

    private void fieldSetValue(Field field, Object instance, Object o) throws IllegalAccessException {
        Class<?> type = field.getType();
        if (type == int.class || type == Integer.class) {
            if (o instanceof BigDecimal) {
                field.set(instance, ((BigDecimal) o).intValue());
                return;
            }
        }
        if (type == long.class || type == Long.class) {
            if (o instanceof BigDecimal) {
                field.set(instance, ((BigDecimal) o).longValue());
                return;
            }
        }
        if (type == float.class || type == Float.class) {
            if (o instanceof BigDecimal) {
                field.set(instance, ((BigDecimal) o).floatValue());
                return;
            }
        }
        if (type == double.class || type == Double.class) {
            if (o instanceof BigDecimal) {
                field.set(instance, ((BigDecimal) o).doubleValue());
                return;
            }
        }
        field.set(instance, o);
    }

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
            return "\"" + (String) obj + "\"";
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
