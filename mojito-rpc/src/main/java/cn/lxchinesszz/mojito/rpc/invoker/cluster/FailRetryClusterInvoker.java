package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.exeception.RpcException;
import cn.lxchinesszz.mojito.rpc.invoker.*;
import com.github.rholder.retry.*;
import io.protostuff.Rpc;
import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 失败进行重试
 *
 * @author liuxin
 * 2022/8/28 22:03
 */
@Slf4j
public class FailRetryClusterInvoker<T> extends AbstractClusterInvoker<T> {

    @SuppressWarnings("all")
    Retryer<Result> invokeRetry = RetryerBuilder.<Result>newBuilder()
            //发生ConnectException异常时重试
            .retryIfExceptionOfType(RpcException.class)
            //重试的等待策略 初始等待1s，每次递增1s。如：第一次1s，第二次2s，第三次3s，以此类推...
            .withWaitStrategy(WaitStrategies.incrementingWait(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS))
            //重试3次后停止
            .withStopStrategy(StopStrategies.stopAfterAttempt(3))
            .withRetryListener(new RetryListener() {
                @Override
                public <V> void onRetry(Attempt<V> attempt) {
                    log.info("FailRetryClusterInvoker: 重试次数:{}", attempt.getAttemptNumber());
                }
            })
            .build();

    public FailRetryClusterInvoker(LoadBalance loadBalance, ServerDiscover serverDiscover, Class<T> interfaceType) {
        super(loadBalance, serverDiscover, interfaceType);
    }

    @Override
    public Result doDirectInvoker(Invoker<T> invoker, Invocation invocation) {
        Result invoke;
        try {
            // 基于重试策略
            invoke = invokeRetry.call(() -> new RetryInvoker(invoker).invoke(invocation));
        } catch (Throwable t) {
            invoke = new RpcResult(t);
        }
        return invoke;
    }

    private class RetryInvoker implements Invoker<T> {

        private Invoker<T> invoker;

        public RetryInvoker(Invoker<T> invoker) {
            this.invoker = invoker;
        }

        @Override
        public Class getInterface() {
            return invoker.getInterface();
        }

        @Override
        public Result invoke(Invocation invocation) throws RpcException {
            return FailRetryClusterInvoker.super.doInvoke(invoker, invocation);
        }
    }

    @Override
    public void doFanoutInvoker(List<Invoker<T>> invokers, Invocation invocation) {
        for (Invoker<T> invoker : invokers) {
            doDirectInvoker(invoker, invocation);
        }
    }
}
