package cn.lxchinesszz.mojito.rpc.banlance.impl;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.invoker.Invocation;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 平均负载,保证每台都进行
 *
 * @author liuxin
 * 2022/9/7 21:48
 */
public class AverageLoadBalance implements LoadBalance {


    private static final AtomicInteger atomicInteger = new AtomicInteger();


    @Override
    public <T> Invoker<T> routeBalance(List<Invoker<T>> invokers, Invocation invocation) {
        int count = atomicInteger.get();
        if (count >= Integer.MAX_VALUE) {
            atomicInteger.set(0);
        }
        atomicInteger.incrementAndGet();
        return invokers.get(count % invokers.size());
    }
}
