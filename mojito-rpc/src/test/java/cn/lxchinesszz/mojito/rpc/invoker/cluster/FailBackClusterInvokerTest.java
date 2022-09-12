package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.Person;
import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.banlance.impl.AverageLoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.directory.impl.LocalServerCenter;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;


/**
 * @author liuxin
 * 2022/9/11 21:19
 */
class FailBackClusterInvokerTest {

    /**
     * 1. 轮换负责 + 本地服务注册
     * 2. 集群容错策略 = 快速失败
     */
    @Test
    @DisplayName("失败返回,后台重试")
    public void localFailBackInvoker() throws Exception {
        // 负责均衡策略
        LoadBalance loadBalance = new AverageLoadBalance();
        // 服务发现,添加上监控的功能,一旦端口连接自己提出,当注册上来后,在增加
        ServerDiscover serverDiscover = new LocalServerCenter<>(Collections.singletonList(new Person() {
            int i = 0;

            @Override
            public String getName() {
                if (i == 0) {
                    i++;
                    throw new RuntimeException("第一次失败");
                } else {
                    System.out.println("第二次运行请求");
                }
                return null;
            }
        }));
        FailBackClusterInvoker clusterInvoker = new FailBackClusterInvoker(loadBalance, serverDiscover,Person.class);
        RpcInvocation invocation = RpcInvocation.builder().interfaceType(Person.class).methodName("getName").arguments(null).build();
        System.out.println(clusterInvoker.invoke(invocation).getValue());
        // 睡眠5秒等待后台
        Thread.sleep(6 * 1000);
    }
}