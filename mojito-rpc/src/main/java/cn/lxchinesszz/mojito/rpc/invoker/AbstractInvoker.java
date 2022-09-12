package cn.lxchinesszz.mojito.rpc.invoker;

import cn.lxchinesszz.mojito.rpc.exeception.RpcException;
import cn.lxchinesszz.mojito.rpc.utils.reflect.JvmInvoker;
import cn.lxchinesszz.mojito.rpc.utils.MethodReflectorCache;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供方法反射缓存的能力
 *
 * @author liuxin
 * 2022/8/28 23:26
 */
public abstract class AbstractInvoker<T> implements Invoker<T> {

    /**
     * 方法缓存
     */
    private final Map<Class<?>, MethodReflectorCache> reflectorCacheMap = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public Class<T> getInterface() {
        return (Class<T>) getSource().getClass();
    }

    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        MethodReflectorCache reflectorCache = reflectorCacheMap.get(invocation.getInterface());
        if (Objects.isNull(reflectorCache)) {
            reflectorCache = new MethodReflectorCache(invocation.getInterface());
            reflectorCacheMap.put(invocation.getInterface(), reflectorCache);
        }
        String methodName = invocation.getMethodName();
        Object[] arguments = invocation.getArguments();
        try {
            JvmInvoker method = reflectorCache.findMethod(methodName, arguments);
            Object invoke = method.invoke(getSource(), arguments);
            return new RpcResult(invoke);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return new RpcResult(e);
        }
    }

    /**
     * 具体执行类
     *
     * @return Object
     */
    public abstract Object getSource();
}
