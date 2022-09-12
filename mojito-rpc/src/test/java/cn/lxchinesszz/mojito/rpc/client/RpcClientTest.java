package cn.lxchinesszz.mojito.rpc.client;

import cn.lxchinesszz.mojito.rpc.Person;
import cn.lxchinesszz.mojito.rpc.User;
import cn.lxchinesszz.mojito.rpc.client.impl.DefaultRpcClient;
import cn.lxchinesszz.mojito.rpc.directory.ServerNode;
import cn.lxchinesszz.mojito.rpc.directory.impl.NetworkRpcServiceCenter;
import cn.lxchinesszz.mojito.rpc.server.impl.DefaultRpcServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;


/**
 * @author liuxin
 * 2022/9/12 00:02
 */
class RpcClientTest {


    @Test
    @DisplayName("客户端")
    public void test() {
        // 因为一台服务器,可能是会依赖服务,也可能提供服务。
        NetworkRpcServiceCenter serviceCenter =
                new NetworkRpcServiceCenter(Collections.singletonList(ServerNode.serverNode("127.0.0.1", 8080, 2)));


        DefaultRpcServer rpcServer = new DefaultRpcServer(serviceCenter);
        rpcServer.startAsync(8080);
        rpcServer.export(new User("jay"));

        serviceCenter.registerInterface(Person.class);


        // 生成客户端
        // SpringRpcClient,实现 SpringRpcClientFactory
        DefaultRpcClient defaultRpcClient = new DefaultRpcClient(serviceCenter);
        Person person = defaultRpcClient.getObject(Person.class);
        System.out.println(person.getName());



    }
}