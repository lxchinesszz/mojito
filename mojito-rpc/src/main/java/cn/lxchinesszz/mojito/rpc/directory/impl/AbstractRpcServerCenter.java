package cn.lxchinesszz.mojito.rpc.directory.impl;

import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.invoker.Invocation;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.directory.DirectoryRegister;
import cn.lxchinesszz.mojito.rpc.utils.EnhanceStream;
import org.checkerframework.checker.units.qual.C;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 因为客户端和服务端的执行都是统一的模型,所以公用
 *
 * @author liuxin
 * 2022/9/7 21:57
 */
public abstract class AbstractRpcServerCenter implements ServerDiscover, DirectoryRegister {

    private final Map<String, List<Invoker<Object>>> serverDiscoverMap = new ConcurrentHashMap<>();

    @SuppressWarnings("all")
    public void registerInvoker(Invoker<?> invoker) {
        Invoker<Object> invokerWrapper = (Invoker<Object>) invoker;
        Class<?> interfaceType = invoker.getInterface();
        if (interfaceType.isInterface()) {
            doRegisterInvoker(interfaceType.toString(), invokerWrapper);
        } else {
            Class<?>[] interfaces = interfaceType.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                doRegisterInvoker(anInterface.toString(), invokerWrapper);
            }
        }
    }

    @Override
    public List<Invoker<Object>> list(Invocation invocation) {
        return serverDiscoverMap.getOrDefault(invocation.getInterface().toString(), new ArrayList<>());
    }

    @Override
    public List<Invoker<Object>> list() {
        List<Invoker<Object>> invokers = EnhanceStream.mergeValues(this.serverDiscoverMap);
        return new ArrayList<>(Collections.unmodifiableCollection(invokers));
    }

    public void doRegisterInvoker(String interfaceType, Invoker<Object> invoker) {
        List<Invoker<Object>> invokers = serverDiscoverMap.get(interfaceType);
        if (Objects.isNull(invokers)) {
            invokers = new ArrayList<>();
            serverDiscoverMap.put(interfaceType, invokers);
        }
        invokers.add(invoker);
    }
}
