package cn.lxchinesszz.mojito.rpc.invoker;

import cn.lxchinesszz.mojito.rpc.exeception.RpcException;

/**
 * @author liuxin
 * 2022/8/17 18:54
 */
public interface Result {

    /**
     * 结果值
     *
     * @return Object
     */
    Object getValue();

    /**
     * 异常信息
     *
     * @return Throwable
     */
    RpcException getException();

    /**
     * 是否有异常
     *
     * @return has exception.
     */
    boolean hasException();
}
