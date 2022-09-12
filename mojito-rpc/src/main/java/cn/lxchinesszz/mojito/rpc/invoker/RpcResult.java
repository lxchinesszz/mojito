package cn.lxchinesszz.mojito.rpc.invoker;

import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.rpc.exeception.RpcException;

import java.util.Objects;

/**
 * @author liuxin
 * 2022/8/28 22:06
 */
public class RpcResult extends ProtocolHeader implements Result {

    private Object value;

    private RpcException throwable;

    public RpcResult(Object value) {
        this.value = value;
    }

    public RpcResult(Throwable throwable) {
        this(new RpcException(throwable));
    }

    public RpcResult(RpcException throwable) {
        this.throwable = throwable;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public RpcException getException() {
        return throwable;
    }

    @Override
    public boolean hasException() {
        return Objects.nonNull(throwable);
    }
}
