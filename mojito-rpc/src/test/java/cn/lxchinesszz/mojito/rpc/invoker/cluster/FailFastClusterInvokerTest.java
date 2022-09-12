package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.Person;
import cn.lxchinesszz.mojito.rpc.User;
import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.directory.impl.LocalServiceCenter;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;


/**
 * @author liuxin
 * 2022/9/11 21:17
 */
class FailFastClusterInvokerTest {

    /**
     * 1. 轮换负责 + 本地服务注册
     * 2. 集群容错策略 = 快速失败
     */
    @Test
    @DisplayName("快速失败,不做任何处理")
    public void localFailFastInvoker() {
        // 负责均衡策略
        LoadBalance loadBalance = new AverageLoadBalance();
        // 服务发现,添加上监控的功能,一旦端口连接自己提出,当注册上来后,在增加
        ServerDiscover serverDiscover = new LocalServiceCenter(Arrays.asList(new User("周杰伦"), new User("谢霆锋")));
        FailFastClusterInvoker<Person> clusterInvoker = new FailFastClusterInvoker<>(loadBalance, serverDiscover,Person.class);
        RpcInvocation invocation = RpcInvocation.builder().interfaceType(Person.class).methodName("getName").arguments(null).build();
        System.out.println(clusterInvoker.invoke(invocation).getValue());
        System.out.println(clusterInvoker.invoke(invocation).getValue());
    }

}