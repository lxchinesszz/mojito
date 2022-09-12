package cn.lxchinesszz.mojito.rpc.invoker;

import cn.lxchinesszz.mojito.rpc.exeception.RpcException;

/**
 * @author liuxin
 * 2022/8/28 20:42
 */
public class RadioResult implements Result{
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public RpcException getException() {
        return null;
    }

    @Override
    public boolean hasException() {
        return false;
    }
}
