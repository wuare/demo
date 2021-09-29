package top.wuare.json.convert;

import top.wuare.json.exception.JsonConvertException;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json convert to java object
 *
 * @author wuare
 * @since 2021/6/18
 */
public class JsonConvert {

    @SuppressWarnings("unchecked")
    public <T> T fromJson(Object obj, Type type) {

        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            if (type == Boolean.class || type == boolean.class) {
                return (T) obj;
            }
        }
        if (obj instanceof String) {
            if (type == String.class) {
                return (T) obj;
            }
        }
        if (obj instanceof BigDecimal) {
            if (type == Integer.class || type == int.class) {
                return (T) Integer.valueOf(((BigDecimal) obj).intValue());
            }
            if (type == Long.class || type == long.class) {
                return (T) Long.valueOf(((BigDecimal) obj).longValue());
            }
            if (type == Float.class || type == float.class) {
                return (T) Float.valueOf(((BigDecimal) obj).floatValue());
            }
            if (type == Double.class || type == double.class) {
                return (T) Double.valueOf(((BigDecimal) obj).doubleValue());
            }
            if (type == BigDecimal.class) {
                return (T) obj;
            }
        }
        if (obj instanceof List) {
            List<?> objList = (List<?>) obj;
            if (type == List.class) {
                return (T) new ArrayList<Object>(objList);
            }
            if (type instanceof Class && List.class.isAssignableFrom((Class<?>) type)) {
                try {
                    List<Object> resList = (List<Object>) ((Class<?>) type).getDeclaredConstructor().newInstance();
                    resList.addAll(objList);
                    return (T) resList;
                } catch (ReflectiveOperationException e) {
                    throw new JsonConvertException(type.getTypeName() + "没有默认构造方法");
                }
            }
            if (type instanceof Class && ((Class<?>) type).isArray()) {
                Class<?> clazz = (Class<?>) type;
                Object array = Array.newInstance(clazz.getComponentType(), objList.size());
                for (int i = 0; i < objList.size(); i++) {
                    Array.set(array, i, fromJson(objList.get(i), clazz.getComponentType()));
                }
                return (T) array;
            }
            if (type instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) type).getRawType();
                Type[] argTypes = ((ParameterizedType) type).getActualTypeArguments();
                if (rawType instanceof Class && List.class.isAssignableFrom((Class<?>) rawType)) {
                    List<?> resList;
                    if (rawType == List.class) {
                        resList = new ArrayList<>();
                    } else {
                        try {
                            resList = (List<?>) ((Class<?>) rawType).getDeclaredConstructor().newInstance();
                        } catch (ReflectiveOperationException e) {
                            throw new JsonConvertException(rawType.getTypeName() + "没有默认构造方法");
                        }
                    }
                    for (Object o : objList) {
                        resList.add(fromJson(o, argTypes[0]));
                    }
                    return (T) resList;
                }
            }
        }
        if (obj instanceof Map) {
            Map<String, Object> objMap = (Map<String, Object>) obj;
            if (type == Map.class) {
                return (T) objMap;
            }
            if (type instanceof Class && Map.class.isAssignableFrom((Class<?>) type)) {
                try {
                    Map<String, Object> resMap = (Map<String, Object>) ((Class<?>) type).getDeclaredConstructor().newInstance();
                    resMap.putAll(objMap);
                    return (T) resMap;
                } catch (ReflectiveOperationException e) {
                    throw new JsonConvertException(((Class<?>) type).getName() + "没有默认构造方法");
                }
            }
            if (type instanceof ParameterizedType) {
                Type rawType = ((ParameterizedType) type).getRawType();
                if (rawType instanceof Class && Map.class.isAssignableFrom((Class<?>) rawType)) {
                    Type[] argTypes = ((ParameterizedType) type).getActualTypeArguments();
                    if (argTypes[0] != String.class) {
                        throw new JsonConvertException("Map泛型key应该为String类型");
                    }
                    Map<?, ?> resMap;
                    if (rawType == Map.class) {
                        resMap = new HashMap<>();
                    } else {
                        try {
                            resMap = (Map<?, ?>) ((Class<?>) rawType).getDeclaredConstructor().newInstance();
                        } catch (ReflectiveOperationException e) {
                            throw new JsonConvertException(rawType.getTypeName() + "没有默认构造方法");
                        }
                    }
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                        resMap.put(fromJson(entry.getKey(), argTypes[0]), fromJson(entry.getValue(), argTypes[1]));
                    }
                    return (T) resMap;
                }

            }
            if (type instanceof Class) {
                Class<?> clazz = (Class<?>) type;
                Field[] fields = clazz.getDeclaredFields();
                Object instance;
                try {
                    instance = clazz.getDeclaredConstructor().newInstance();
                } catch (ReflectiveOperationException e) {
                    throw new JsonConvertException(clazz.getName() + "没有默认的构造方法", e);
                }
                for (Field f : fields) {
                    try {
                        PropertyDescriptor descriptor = new PropertyDescriptor((String) f.getName(), clazz);
                        Method writeMethod = descriptor.getWriteMethod();
                        Object o = objMap.get(descriptor.getName());
                        if (o == null) {
                            continue;
                        }
                        Object arg = fromJson(o, f.getGenericType());
                        writeMethod.invoke(instance, arg);
                    } catch (IntrospectionException e) {
                        System.out.println(e.getMessage());
                    } catch (ReflectiveOperationException e) {
                        System.out.println("设置值错误" + e.getMessage());
                    }
                }
                return (T) instance;
            }
        }
        throw new JsonConvertException(type.getTypeName() + "不支持反序列化");
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
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
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
