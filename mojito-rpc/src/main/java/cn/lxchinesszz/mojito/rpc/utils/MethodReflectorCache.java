package cn.lxchinesszz.mojito.rpc.utils;

import cn.lxchinesszz.mojito.rpc.utils.reflect.JvmInvoker;
import cn.lxchinesszz.mojito.rpc.utils.reflect.MethodInvoker;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxin
 * 2022/8/28 23:47
 */
public class MethodReflectorCache {
    private final Class<?> type;
    private final Map<String, JvmInvoker> methodMap = new ConcurrentHashMap<>();

    public MethodReflectorCache(Class<?> type) {
        this.type = type;
    }

    public static String buildMethodSignature(String methodName, Class<?>... parameterTypes) {
        StringJoiner stringJoiner = new StringJoiner("_");
        stringJoiner.add(methodName);
        if (Objects.nonNull(parameterTypes)) {
            for (Class<?> parameterType : parameterTypes) {
                stringJoiner.add(parameterType.getTypeName());
            }
        }
        return stringJoiner.toString();
    }

    public static String buildMethodSignature(String methodName, Object[] arguments) {
        return buildMethodSignature(methodName, EnhanceStream.toClassArray(arguments));
    }

    public JvmInvoker findMethod(String methodName, Object[] arguments) throws NoSuchMethodException {
        Class<?>[] classes = EnhanceStream.toClassArray(arguments);
        return findMethodByClass(methodName, classes);
    }

    private JvmInvoker findMethodByClass(String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException {
        String methodSignature = buildMethodSignature(methodName, parameterTypes);
        JvmInvoker jvmInvoker = methodMap.get(methodSignature);
        if (Objects.isNull(jvmInvoker)) {
            Method method = type.getDeclaredMethod(methodName, parameterTypes);
            jvmInvoker = new MethodInvoker(method);
            methodMap.put(methodName, jvmInvoker);
        }
        return jvmInvoker;
    }
}
