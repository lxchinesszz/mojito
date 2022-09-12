package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.invoker.*;
import com.hanframework.kit.thread.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 失败也返回,失败后异步重试
 * 1. 如果失败加入到异步重试队列中,定期重试
 *
 * @author liuxin
 * 2022/8/28 22:03
 */
@Slf4j
public class FailBackClusterInvoker<T> extends AbstractClusterInvoker<T> {

    private static final long RETRY_FAILED_PERIOD = 5 * 1000;

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2,
            new NamedThreadFactory("failback-cluster-timer", true));


    public FailBackClusterInvoker(LoadBalance loadBalance, ServerDiscover serverDiscover, Class<T> interfaceType) {
        super(loadBalance, serverDiscover, interfaceType);
    }

    /**
     * 添加到异步队列中
     *
     * @param invoker    执行器
     * @param invocation 执行参数
     */
    public void addRetryTask(Invoker<T> invoker, Invocation invocation) {
        scheduledExecutorService.schedule(() -> {
                    log.info("FailBackClusterInvoker:{}.{}", invocation.getInterface(), invocation.getMethodName());
                    invoker.invoke(invocation);
                }
                , RETRY_FAILED_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    public Result doDirectInvoker(Invoker<T> invoker, Invocation invocation) {
        Result invoke;
        try {
            invoke = doInvoke(invoker, invocation);
        } catch (Throwable t) {
            // 添加到队列中
            addRetryTask(invoker, invocation);
            // 如果失败,构建一个FailBackResult
            invoke = new FailBackResult();
        }
        return invoke;
    }

    @Override
    public void doFanoutInvoker(List<Invoker<T>> invokers, Invocation invocation) {
        for (Invoker<T> invoker : invokers) {
            doDirectInvoker(invoker, invocation);
        }
    }
}
