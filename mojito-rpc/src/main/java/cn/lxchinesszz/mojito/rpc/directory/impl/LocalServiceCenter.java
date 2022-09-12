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
public class LocalServiceCenter extends AbstractRpcServiceCenter {


    private List<Object> localTarget;

    public LocalServiceCenter() {
    }

    public LocalServiceCenter(List<?> localTarget) {
        for (Object target : localTarget) {
            registerInvoker(localInvoker(target));
        }
    }

    protected Invoker<Object> localInvoker(Object target) {
        JdkProxyFactory proxyFactory = new JdkProxyFactory();
        return proxyFactory.getLocalInvoker(target);
    }

    public List<Object> getLocalTarget() {
        return this.localTarget;
    }
}
