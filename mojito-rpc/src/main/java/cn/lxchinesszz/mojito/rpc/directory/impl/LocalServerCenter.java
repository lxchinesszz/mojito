package cn.lxchinesszz.mojito.rpc.directory.impl;

import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.proxy.JdkProxyFactory;

import java.util.List;

/**
 * 自定义节点
 *
 * @author liuxin
 * 2022/9/9 21:16
 */
public class LocalServerCenter<T> extends AbstractRpcServerCenter {


    private List<T> localTarget;

    public LocalServerCenter(List<T> localTarget) {
        JdkProxyFactory proxyFactory = new JdkProxyFactory();
        for (T target : localTarget) {
            Invoker<T> localInvoker = proxyFactory.getLocalInvoker(target);
            registerInvoker(localInvoker);
        }
    }

    public List<T> getLocalTarget() {
        return this.localTarget;
    }
}
