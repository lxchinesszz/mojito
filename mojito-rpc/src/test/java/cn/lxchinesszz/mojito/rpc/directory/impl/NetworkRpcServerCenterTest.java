package cn.lxchinesszz.mojito.rpc.directory.impl;

import cn.lxchinesszz.mojito.rpc.Person;
import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerNode;
import cn.lxchinesszz.mojito.rpc.invoker.cluster.FailFastClusterInvoker;
import cn.lxchinesszz.mojito.rpc.server.RpcDispatchServer;
import org.junit.jupiter.api.Test;

import java.util.Collections;


/**
 * @author liuxin
 * 2022/9/11 20:06
 */
class NetworkRpcServerCenterTest {

    @Test
    public void test() {

        // 如果只是客户端,这样就可以了。
        // RpcClient rpcClient = new RpcClient();
        NetworkRpcServerCenter serverCenter =
                new NetworkRpcServerCenter(Collections.singletonList(ServerNode.serverNode("127.0.0.1", 8080, 2)));
        serverCenter.registerInterface(Person.class);

        Person remoteProxy = serverCenter.getRemoteProxy(Person.class);


        RpcDispatchServer rpcDispatchServer = new RpcDispatchServer(serverCenter);
        rpcDispatchServer.start(8080);


        LoadBalance loadBalance = new AverageLoadBalance();
        FailFastClusterInvoker<Person> clusterInvoker = new FailFastClusterInvoker<>(loadBalance, serverCenter,Person.class);


    }

}