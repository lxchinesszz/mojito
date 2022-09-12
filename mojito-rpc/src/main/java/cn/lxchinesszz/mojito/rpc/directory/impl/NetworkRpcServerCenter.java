package cn.lxchinesszz.mojito.rpc.directory.impl;

import cn.lxchinesszz.mojito.net.client.Client;
import cn.lxchinesszz.mojito.net.fluent.Mojito;
import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerNode;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import cn.lxchinesszz.mojito.rpc.invoker.RpcResult;
import cn.lxchinesszz.mojito.rpc.invoker.cluster.FailFastClusterInvoker;
import cn.lxchinesszz.mojito.rpc.proxy.JdkProxyFactory;
import cn.lxchinesszz.mojito.rpc.proxy.ProxyFactory;

import java.util.List;

/**
 * 通过配置的方式将服务节点添加进来
 * ServerNodeRpcServerCenter
 *
 * @author liuxin
 * 2022/9/11 19:19
 */
public class NetworkRpcServerCenter extends AbstractRpcServerCenter {

    private final List<ServerNode> serverNodes;

    private final ProxyFactory proxyFactory;

    public NetworkRpcServerCenter(List<ServerNode> serverNodes) {
        this.serverNodes = serverNodes;
        this.proxyFactory = new JdkProxyFactory();
    }

    public List<ServerNode> getServerNodes() {
        return serverNodes;
    }

    /**
     * 获取接口能力,生成集群的失败策略
     *
     * @param interfaceType 服务类型
     * @return T
     */
    @SuppressWarnings("all")
    public <T> T getRemoteProxy(Class<T> interfaceType) {
        Invoker clusterInvoker = new FailFastClusterInvoker(new AverageLoadBalance(), this, interfaceType);
        return (T) proxyFactory.getRemoteProxy(clusterInvoker);
    }

    /**
     * 远程接口的注册
     *
     * @param interfaceType 接口完整限定名
     */
    public void registerInterface(Class<?> interfaceType) {
        for (ServerNode serverNode : getServerNodes()) {
            Client<RpcInvocation, RpcResult> client;
            try {
                client = Mojito.client(RpcInvocation.class, RpcResult.class).connect(serverNode.getHost(), serverNode.getPort());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Invoker<?> remoteInvoker = proxyFactory.getRemoteInvoker(client, interfaceType);
            doRegisterInvoker(interfaceType.toString(), (Invoker<Object>) remoteInvoker);
        }
    }


}
