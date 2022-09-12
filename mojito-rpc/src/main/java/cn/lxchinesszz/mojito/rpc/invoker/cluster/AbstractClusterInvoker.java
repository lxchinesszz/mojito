package cn.lxchinesszz.mojito.rpc.invoker.cluster;

import cn.lxchinesszz.mojito.rpc.banlance.LoadBalance;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.exeception.RpcException;
import cn.lxchinesszz.mojito.rpc.invoker.*;
import cn.lxchinesszz.mojito.rpc.utils.EnhanceStream;
import io.protostuff.Rpc;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 集群
 *
 * @author liuxin
 * 2022/8/28 20:36
 */
public abstract class AbstractClusterInvoker<T> implements Invoker<T> {

    /**
     * 负载均衡策略
     */
    private final LoadBalance loadBalance;

    /**
     * 服务发现,可以从网络进行发现,也可以本地写死
     */
    private final ServerDiscover serverDiscover;

    /**
     * 客户端都是从服务发现中获取实例,只能在执行过程中找到对象的实力类型
     * 所以这里为了让集群Invoker具有和本地Invoker一样的获取接口的能力,要在这里通过参数的形式获取
     */
    private final Class<T> interfaceType;


    public AbstractClusterInvoker(LoadBalance loadBalance, ServerDiscover serverDiscover, Class<T> interfaceType) {
        this.loadBalance = loadBalance;
        this.serverDiscover = serverDiscover;
        this.interfaceType = interfaceType;
    }

    @Override
    public Class<T> getInterface() {
        return this.interfaceType;
    }


    @Override
    public Result invoke(Invocation invocation) throws RpcException {
        // 获取执行策略
        String radio = invocation.getAttachment("fanout", "false");
        List<Invoker<Object>> list = serverDiscover.list(invocation);
        if (EnhanceStream.isEmpty(list)) {
            throw new RpcException("未发现服务:" + invocation.getInterface().toString());
        }
        List<Invoker<T>> invokers = mapperTList(serverDiscover.list(invocation));
        // 广播模式
        if (Boolean.getBoolean(radio)) {
            // 获取所有可用执行器
            doFanoutInvoker(invokers, invocation);
            return new RadioResult();
        } else {
            Invoker<T> invoker = routeBalance(invokers, invocation);
            return doDirectInvoker(invoker, invocation);
        }
    }

    /**
     * 获取所有的可执行类,保证每次获取都是最新的
     *
     * @param invocation 请求参数
     * @return List<Invoker < T>>
     */
    public List<Invoker<T>> getInvokers(Invocation invocation) {
        return mapperTList(serverDiscover.list(invocation));
    }


    /**
     * 服务中心服务多实例对象,转换成当前泛型的对象
     * eg: 服务中心有,Person、Animal 两种实力
     * 当前泛型是Animal,所以这里就只能拿到Animal
     *
     * @param sourceList
     * @return
     */
    @SuppressWarnings("all")
    public List<Invoker<T>> mapperTList(List<Invoker<Object>> sourceList) {
        return sourceList.stream().map(o -> (Invoker<T>) o).collect(Collectors.toList());
    }

    /**
     * 负载均衡策略
     *
     * @param invokers   所有可用执行器
     * @param invocation 远程执行参数
     * @return Invoker<T>
     */
    public Invoker<T> routeBalance(List<Invoker<T>> invokers, Invocation invocation) {
        return loadBalance.routeBalance(invokers, invocation);
    }

    /**
     * Direct单点模式,找到指定的执行器进行执行,子类需要实现自己的容错策略
     *
     * @param invoker    执行器
     * @param invocation 执行器远程参数
     * @return Result
     */
    protected abstract Result doDirectInvoker(Invoker<T> invoker, Invocation invocation);

    /**
     * Fanout广播模式,向所有的执行器进行执行,子类需要实现自己的容错策略
     *
     * @param invokers   执行器集合
     * @param invocation 执行器远程参数
     */
    protected abstract void doFanoutInvoker(List<Invoker<T>> invokers, Invocation invocation);

    protected Result doInvoke(Invoker<T> invoker, Invocation invocation) {
        Result clusterResult = invoker.invoke(invocation);
        if (clusterResult.hasException()) {
            throw clusterResult.getException();
        }
        return clusterResult;
    }
}
