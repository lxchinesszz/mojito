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
 * 2022/9/11 21:18
 */
class FailoverClusterInvokerTest {

    /**
     * 1. 轮换负责 + 本地服务注册
     * 2. 集群容错策略 = 失败转移
     */
    @Test
    @DisplayName("失败转移策略")
    public void localFailOverInvoker() {
        // 负责均衡策略
        LoadBalance loadBalance = new AverageLoadBalance();
        // 服务发现,添加上监控的功能,一旦端口连接自己提出,当注册上来后,在增加
        ServerDiscover serverDiscover = new LocalServiceCenter(Arrays.asList(new Person() {
            @Override
            public String getName() {
                throw new RuntimeException();
            }
        }, new User("谢霆锋")));
        FailoverClusterInvoker<Person> clusterInvoker = new FailoverClusterInvoker<>(loadBalance, serverDiscover,Person.class);
        RpcInvocation invocation = RpcInvocation.builder().interfaceType(Person.class).methodName("getName").arguments(null).build();
        // 失败进行转移,打印两次谢霆锋
        System.out.println(clusterInvoker.invoke(invocation).getValue());
        System.out.println(clusterInvoker.invoke(invocation).getValue());
    }

}