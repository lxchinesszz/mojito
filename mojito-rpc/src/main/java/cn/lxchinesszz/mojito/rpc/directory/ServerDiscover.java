package cn.lxchinesszz.mojito.rpc.directory;

import cn.lxchinesszz.mojito.rpc.invoker.Invocation;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;

import java.util.List;

/**
 * 服务发现
 *
 * @author liuxin
 * 2022/8/28 20:15
 */
public interface ServerDiscover {

    /**
     * 客户端通过接口,获取网络上所有可用的Invoker
     *
     * @param invocation 类型
     * @return List<Invoker < T>> 可用的执行器
     */
    List<Invoker<Object>> list(Invocation invocation);

    /**
     * 获取所有的执行体
     *
     * @return List<Invoker < T>>
     */
    List<Invoker<Object>> list();
}
