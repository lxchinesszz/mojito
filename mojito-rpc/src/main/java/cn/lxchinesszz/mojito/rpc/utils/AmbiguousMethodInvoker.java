package cn.lxchinesszz.mojito.rpc.utils;

import cn.lxchinesszz.mojito.rpc.exeception.ReflectionException;
import cn.lxchinesszz.mojito.rpc.utils.reflect.MethodInvoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author liuxin
 * 2022/8/28 23:35
 */
public class AmbiguousMethodInvoker extends MethodInvoker {

    private final String exceptionMessage;

    public AmbiguousMethodInvoker(Method method, String exceptionMessage) {
        super(method);
        this.exceptionMessage = exceptionMessage;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        throw new ReflectionException(exceptionMessage);
    }
}
