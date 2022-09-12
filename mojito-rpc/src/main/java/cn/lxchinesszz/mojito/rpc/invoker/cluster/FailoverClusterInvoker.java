package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.invoker.*;

import java.util.List;

/**
 * 失败转移,只要失败就换下一个,直到所有集群节点都失败为止。
 *
 * @author liuxin
 * 2022/8/28 22:03
 */
public class FailoverClusterInvoker<T> extends AbstractClusterInvoker<T> {

    public FailoverClusterInvoker(LoadBalance loadBalance, ServerDiscover serverDiscover, Class<T> interfaceType) {
        super(loadBalance, serverDiscover, interfaceType);
    }

    @Override
    public Result doDirectInvoker(Invoker<T> invoker, Invocation invocation) {
        Result invoke;
        try {
            // 基于重试策略
            invoke = doInvoke(invoker, invocation);
        } catch (Throwable t) {
            invoke = failover(invoker, invocation);
        }
        return invoke;
    }

    /**
     * 失败转移
     *
     * @param invoker    当前执行器
     * @param invocation 执行参数
     * @return Result 执行结果
     */
    public Result failover(Invoker<T> invoker, Invocation invocation) {
        List<Invoker<T>> invokers = getInvokers(invocation);
        Throwable lastThrowable = null;
        for (int i = 0; i < invokers.size(); i++) {
            Invoker<T> overInvoker = invokers.get(i);
            if (!overInvoker.equals(invoker)) {
                try {
                    // 成功了直接返回,失败了,继续轮训
                    return overInvoker.invoke(invocation);
                } catch (Throwable t) {
                    // 最后一个异常
                    lastThrowable = t;
                }
            }
        }
        return new RpcResult(lastThrowable);
    }

    @Override
    public void doFanoutInvoker(List<Invoker<T>> invokers, Invocation invocation) {
        for (Invoker<T> invoker : invokers) {
            doDirectInvoker(invoker, invocation);
        }
    }
}
