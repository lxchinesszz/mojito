package cn.lxchinesszz.mojito.rpc.banlance.impl;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.invoker.Invocation;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;

import java.util.List;
import java.util.Random;

/**
 * @author liuxin
 * 2022/9/7 21:50
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public <T> Invoker<T> routeBalance(List<Invoker<T>> invokers, Invocation invocation) {
        Random random = new Random();
        return invokers.get(random.nextInt(invokers.size()));
    }
}
