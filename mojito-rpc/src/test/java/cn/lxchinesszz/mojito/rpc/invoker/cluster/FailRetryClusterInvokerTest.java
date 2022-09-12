package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.Person;
import cn.lxchinesszz.mojito.rpc.User;
import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.directory.impl.LocalServerCenter;
import cn.lxchinesszz.mojito.rpc.exeception.RpcException;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liuxin
 * 2022/9/11 21:34
 */
class FailRetryClusterInvokerTest {


    /**
     * 1. 轮换负责 + 本地服务注册
     * 2. 集群容错策略 = 失败转移
     */
    @Test
    @DisplayName("失败重试次数【间隔1s,2s】")
    public void localFailRetryInvoker() throws Exception {
        // 负责均衡策略
        LoadBalance loadBalance = new AverageLoadBalance();
        // 服务发现,添加上监控的功能,一旦端口连接自己提出,当注册上来后,在增加
        ServerDiscover serverDiscover = new LocalServerCenter<>(Arrays.asList(new Person() {

            AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public String getName() {
                // 只针对Rpc异常
                throw new RpcException();
            }
        }, new User("谢霆锋")));
        FailRetryClusterInvoker<Person> clusterInvoker = new FailRetryClusterInvoker<>(loadBalance, serverDiscover,Person.class);
        RpcInvocation invocation = RpcInvocation.builder().interfaceType(Person.class).methodName("getName").arguments(null).build();
        // 失败进行转移,打印两次谢霆锋
        System.out.println(clusterInvoker.invoke(invocation).getValue());
        // 睡眠5秒等待后台
        Thread.sleep(6 * 1000);
    }


}