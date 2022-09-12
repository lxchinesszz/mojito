package cn.lxchinesszz.mojito.rpc.utils.reflect;

import java.lang.reflect.InvocationTargetException;

/**
 * @author liuxin
 * 2022/8/28 23:32
 */
public interface JvmInvoker {

    Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException;

    Class<?> getType();
}
