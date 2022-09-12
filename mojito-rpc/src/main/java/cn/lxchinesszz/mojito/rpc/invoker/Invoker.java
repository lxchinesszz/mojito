package cn.lxchinesszz.mojito.rpc.invoker;

import cn.lxchinesszz.mojito.rpc.exeception.RpcException;

/**
 * @author liuxin
 * 2022/8/17 18:48
 */
public interface Invoker<T> {

    /**
     * 服务接口
     *
     * @return Class<T>
     */
    Class<T> getInterface();

    /**
     * 远程接口调用
     *
     * @param invocation 接口请求参数
     * @return Result
     * @throws RpcException Rpc异常
     */
    Result invoke(Invocation invocation) throws RpcException;
}
