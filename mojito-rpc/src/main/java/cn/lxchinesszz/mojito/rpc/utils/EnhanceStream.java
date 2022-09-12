package cn.lxchinesszz.mojito.rpc.utils;

import cn.lxchinesszz.mojito.rpc.invoker.Invoker;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author liuxin
 * 2022/9/7 20:26
 */
public final class EnhanceStream {

    public static <T> void safeForEach(Collection<T> collection, Consumer<T> consumer) {
        if (isNotEmpty(collection)) {
            collection.forEach(consumer);
        }
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Collection<?> collection) {
        if (Objects.isNull(collection) || collection.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static Class<?>[] toClassArray(Object[] arguments) {
        if (Objects.nonNull(arguments) && arguments.length > 0) {
            Class<?>[] result = new Class[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                result[i] = arguments[i].getClass();
            }
            return result;
        } else {
            return null;
        }
    }

    public static boolean isEqualsMethod(Method method) {
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }


    public static boolean isHashCodeMethod(Method method) {
        return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
    }


    public static boolean isToStringMethod(Method method) {
        return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
    }

    public static <K, V> List<V> mergeValues(Map<K, List<V>> map) {
        Collection<List<V>> values = map.values();
        Set<V> result = new LinkedHashSet<>();
        for (List<V> value : values) {
            result.addAll(value);
        }
        return new ArrayList<>(result);
    }
}
