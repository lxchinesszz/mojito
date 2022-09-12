package cn.lxchinesszz.mojito.rpc.banlance;

import cn.lxchinesszz.mojito.rpc.invoker.Invocation;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;

import java.util.List;

/**
 * 负载均衡
 *
 * @author liuxin
 * 2022/8/28 20:07
 */
public interface LoadBalance {

    <T> Invoker<T> routeBalance(List<Invoker<T>> invokers, Invocation invocation);
}
