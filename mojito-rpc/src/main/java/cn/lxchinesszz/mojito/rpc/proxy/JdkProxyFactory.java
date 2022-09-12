package cn.lxchinesszz.mojito.rpc.proxy;

import cn.lxchinesszz.mojito.net.client.Client;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.MojitoInvoker;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import cn.lxchinesszz.mojito.rpc.invoker.RpcResult;
import cn.lxchinesszz.mojito.rpc.invoker.cluster.FailFastClusterInvoker;
import cn.lxchinesszz.mojito.rpc.utils.EnhanceStream;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;


/**
 * @author liuxin
 * 2022/9/7 20:09
 */
public class JdkProxyFactory implements ProxyFactory {

    /**
     * 服务端使用,生成服务端执行层统一模型
     *
     * @param target 接口
     * @param <T>    泛型
     * @return Invoker<T>
     */
    @Override
    public <T> Invoker<T> getLocalInvoker(T target) {
        return new MojitoInvoker<>(target);
    }

    /**
     * 客户端使用,基于集群实例生成远程执行对象
     *
     * @param client 实例请求对象
     * @param target 接口
     * @param <T>    泛型
     * @return Invoker<T>
     */
    @Override
    public <T> Invoker<T> getRemoteInvoker(Client<RpcInvocation, RpcResult> client, Class<T> target) {
        Object targetObj = getRemoteProxy(client, target);
        return new MojitoInvoker<>(targetObj);
    }

    @Override
    @SuppressWarnings("all")
    public <T> T getRemoteProxy(Client<RpcInvocation, RpcResult> client, Class<T> target) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{target}, (proxy, method, args) -> {
            if (method.isDefault()) {
                return method.invoke(proxy, args);
            } else {
                if (EnhanceStream.isEqualsMethod(method) || EnhanceStream.isToStringMethod(method) || EnhanceStream.isHashCodeMethod(method)) {
                    return method.invoke(proxy, args);
                } else {
                    ProtocolHeader rpcHeader = client.sendAsync(RpcInvocation.builder().
                            interfaceType(target).arguments(args).methodName(method.getName()).build()).get();
                    if (Objects.nonNull(rpcHeader.getException())) {
                        throw rpcHeader.getException();
                    } else {
                        return ((RpcResult) rpcHeader).getValue();
                    }
                }
            }
        });
    }

    @Override
    @SuppressWarnings("all")
    public <T> T getRemoteProxy(Invoker<T> invoker) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{invoker.getInterface()}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcInvocation invocation = RpcInvocation.builder().
                        interfaceType(invoker.getInterface()).arguments(args).methodName(method.getName()).build();
                return invoker.invoke(invocation).getValue();
            }
        });
    }
}
