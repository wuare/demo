package top.wuare.json.convert;

import top.wuare.json.exception.JsonConvertException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    public <T> T fromJson(Object obj, Class<T> clazz) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            if (clazz == Boolean.class || clazz == boolean.class) {
                return (T) obj;
            }
        }
        if (obj instanceof String) {
            if (clazz == String.class) {
                return (T) obj;
            }
        }
        if (obj instanceof BigDecimal) {
            if (clazz == Integer.class || clazz == int.class) {
                return (T) Integer.valueOf(((BigDecimal) obj).intValue());
            }
            if (clazz == Long.class || clazz == long.class) {
                return (T) Long.valueOf(((BigDecimal) obj).longValue());
            }
            if (clazz == Float.class || clazz == float.class) {
                return (T) Float.valueOf(((BigDecimal) obj).floatValue());
            }
            if (clazz == Double.class || clazz == double.class) {
                return (T) Double.valueOf(((BigDecimal) obj).doubleValue());
            }
        }
        if (List.class.isAssignableFrom(clazz)) {
            throw new JsonConvertException("暂不支持List反序列化");
        }
        if (Map.class.isAssignableFrom(clazz)) {
            throw new JsonConvertException("暂不支持Map反序列化");
        }
        if (obj instanceof List) {
            if (clazz.isArray()) {
                List<?> list = (List<?>) obj;
                Object array = Array.newInstance(clazz.getComponentType(), list.size());
                for (int i = 0; i < list.size(); i++) {
                    Array.set(array, i, fromJson(list.get(i), clazz.getComponentType()));
                }
                return (T) array;
            }
        }
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            Field[] fields = clazz.getDeclaredFields();
            T t;
            try {
                t = clazz.getDeclaredConstructor().newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new JsonConvertException(clazz.getName() + "没有默认的构造方法", e);
            }
            for (Field f : fields) {
                try {
                    PropertyDescriptor descriptor = new PropertyDescriptor((String) f.getName(), clazz);
                    Method writeMethod = descriptor.getWriteMethod();
                    Object o = map.get(descriptor.getName());
                    if (o == null) {
                        continue;
                    }
                    writeMethod.invoke(t, fromJson(o, f.getType()));
                } catch (IntrospectionException e) {
                    System.out.println(e.getMessage());
                } catch (ReflectiveOperationException e) {
                    System.out.println("设置值错误" + e.getMessage());
                }

            }
            return t;
        }
        throw new JsonConvertException(clazz.getName() + "不支持反序列化");
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
            return "\"" + obj + "\"";
        }
        if (obj.getClass().isArray()) {
            StringBuilder s0 = new StringBuilder("[");
            int length = Array.getLength(obj);
            for (int i = 0; i < length; i++) {
                s0.append(toJson(Array.get(obj, i))).append(",");
            }
            if (s0.length() > 1) {
                s0.setLength(s0.length() - 1);
            }
            return s0.append("]").toString();
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
                PropertyDescriptor descriptor = new PropertyDescriptor((String) f.getName(), obj.getClass());
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
