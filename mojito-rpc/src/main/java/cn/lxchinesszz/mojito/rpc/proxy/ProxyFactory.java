package cn.lxchinesszz.mojito.rpc.proxy;


import cn.lxchinesszz.mojito.net.client.Client;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import cn.lxchinesszz.mojito.rpc.invoker.RpcResult;

/**
 * @author liuxin
 * 2022/8/23 23:52
 */
public interface ProxyFactory {

    /**
     * 服务端使用,生成服务端执行层统一模型
     *
     * @param target 接口
     * @param <T>    泛型
     * @return Invoker<T>
     */
    <T> Invoker<T> getLocalInvoker(T target);

    /**
     * 客户端使用,基于集群实例生成远程执行对象
     *
     * @param client 实例请求对象
     * @param target 接口
     * @param <T>    泛型
     * @return Invoker<T>
     */
    <T> Invoker<T> getRemoteInvoker(Client<RpcInvocation, RpcResult> client, Class<T> target);

    /**
     * 生成远程调用对象
     *
     * @param target 目标对象
     * @param <T>    泛型
     * @return T
     */
    <T> T getRemoteProxy(Client<RpcInvocation, RpcResult> client, Class<T> target);


    <T> T getRemoteProxy(Invoker<T> invoker);
}
