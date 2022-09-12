package cn.lxchinesszz.mojito.rpc.client.impl;

import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.client.RpcClient;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.cluster.FailFastClusterInvoker;
import cn.lxchinesszz.mojito.rpc.proxy.JdkProxyFactory;
import cn.lxchinesszz.mojito.rpc.proxy.ProxyFactory;

/**
 * @author liuxin
 * 2022/9/11 23:56
 */
public class DefaultRpcClient implements RpcClient {

    private final ProxyFactory proxyFactory;

    private final ServerDiscover serverDiscover;

    public DefaultRpcClient(ServerDiscover serverDiscover) {
        this.proxyFactory = new JdkProxyFactory();
        this.serverDiscover = serverDiscover;
    }

    @Override
    @SuppressWarnings("all")
    public <T> T getObject(Class<T> interfaceType) {
        Invoker<T> clusterInvoker = new FailFastClusterInvoker(new AverageLoadBalance(), this.serverDiscover,interfaceType);
        return (T) proxyFactory.getRemoteProxy(clusterInvoker);
    }
}
