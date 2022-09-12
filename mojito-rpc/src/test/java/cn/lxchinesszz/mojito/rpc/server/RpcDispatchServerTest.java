package cn.lxchinesszz.mojito.rpc.server;

import cn.lxchinesszz.mojito.net.client.Client;
import cn.lxchinesszz.mojito.net.fluent.Mojito;
import cn.lxchinesszz.mojito.rpc.Person;
import cn.lxchinesszz.mojito.rpc.User;
import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.banlance.impl.RandomLoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.directory.impl.CustomerInvokerServerCenter;
import cn.lxchinesszz.mojito.rpc.directory.impl.LocalServerCenter;
import cn.lxchinesszz.mojito.rpc.invoker.*;
import cn.lxchinesszz.mojito.rpc.invoker.cluster.FailFastClusterInvoker;
import cn.lxchinesszz.mojito.rpc.invoker.cluster.FailoverClusterInvoker;
import cn.lxchinesszz.mojito.rpc.proxy.JdkProxyFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;


/**
 * @author liuxin
 * 2022/9/7 23:02
 */
class RpcDispatchServerTest {

    @Test
    void start() throws Exception {


        Client<RpcInvocation, RpcResult> connect = Mojito.client(RpcInvocation.class, RpcResult.class).connect("127.0.0.1", 8080);
        Person remoteProxy = new JdkProxyFactory().getRemoteProxy(connect, Person.class);
        System.out.println(remoteProxy.getName());

        // 负责均衡策略
        LoadBalance loadBalance = new RandomLoadBalance();
        // 服务发现,添加上监控的功能,一旦端口连接自己提出,当注册上来后,在增加
        ServerDiscover serverDiscover = new CustomerInvokerServerCenter<>(Collections.singletonList(new MojitoInvoker<Person>(remoteProxy)));

        RpcDispatchServer rpcDispatchServer = new RpcDispatchServer(serverDiscover);
        rpcDispatchServer.start(8080);

        // 服务发现
        FailFastClusterInvoker<Person> clusterInvoker = new FailFastClusterInvoker<>(loadBalance, serverDiscover,Person.class);
        RpcInvocation invocation = RpcInvocation.builder().interfaceType(Person.class).methodName("getName").arguments(null).build();
        Result result = clusterInvoker.invoke(invocation);
        System.out.println(result.getValue());


//        Invoker<Person> remoteInvoker = new JdkProxyFactory().getRemoteInvoker(connect, Person.class);
//        RpcInvocation invocation = RpcInvocation.builder().interfaceType(Person.class).methodName("getName").arguments(null).build();
//        System.out.println(remoteInvoker.invoke(invocation));
//        Result invoke = remoteInvoker.invoke(invocation);
//        if (invoke.hasException()) {
//            System.out.println(invoke.getException());
//        } else {
//            System.out.println(invoke.getValue());
//        }
//        return (RpcResult) new FailFastClusterInvoker<>(randomLoadBalance, DispatchServer.this).invoke(request);

    }

}