package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.invoker.*;

import java.util.List;

/**
 * 快速失败策略
 * 1. 如果失败直接返回
 *
 * @author liuxin
 * 2022/8/28 22:03
 */
public class FailFastClusterInvoker<T> extends AbstractClusterInvoker<T> {


    public FailFastClusterInvoker(LoadBalance loadBalance, ServerDiscover serverDiscover, Class<T> interfaceType) {
        super(loadBalance, serverDiscover, interfaceType);
    }

    @Override
    public Result doDirectInvoker(Invoker<T> invoker, Invocation invocation) {
        Result invoke;
        try {
            invoke = doInvoke(invoker,invocation);
        } catch (Throwable t) {
            invoke = new RpcResult(t);
        }
        return invoke;
    }

    @Override
    protected void doFanoutInvoker(List<Invoker<T>> invokers, Invocation invocation) {
        for (Invoker<T> invoker : invokers) {
            doDirectInvoker(invoker, invocation);
        }
    }
}
