package cn.lxchinesszz.mojito.rpc.utils.reflect;

import cn.lxchinesszz.mojito.rpc.utils.PropertiesReflector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author liuxin
 * 2022/8/28 23:30
 */
public class MethodInvoker implements JvmInvoker {
    private final Class<?> type;
    private final Method method;

    public MethodInvoker(Method method) {
        this.method = method;
        this.method.setAccessible(true);
        if (method.getParameterTypes().length == 1) {
            type = method.getParameterTypes()[0];
        } else {
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException e) {
            if (PropertiesReflector.canControlMemberAccessible()) {
                method.setAccessible(true);
                return method.invoke(target, args);
            } else {
                throw e;
            }
        }
    }

    @Override
    public Class<?> getType() {
        return type;
    }
}
