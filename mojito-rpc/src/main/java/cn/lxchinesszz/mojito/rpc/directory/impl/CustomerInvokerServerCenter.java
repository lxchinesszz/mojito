package cn.lxchinesszz.mojito.rpc.directory.impl;

import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.utils.EnhanceStream;

import java.util.List;

/**
 * 自定义节点
 *
 * @author liuxin
 * 2022/9/9 21:16
 */
public class CustomerInvokerServerCenter<T> extends AbstractRpcServerCenter {


    private List<Invoker<T>> invokers;

    public CustomerInvokerServerCenter(List<Invoker<T>> invokers) {
        this.invokers = invokers;
        EnhanceStream.safeForEach(invokers, this::registerInvoker);
    }

    public List<Invoker<T>> getMockInvokers() {
        return invokers;
    }
}
