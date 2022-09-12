package cn.lxchinesszz.mojito.rpc.exeception;

/**
 * @author liuxin
 * 2022/8/17 18:54
 */
public class RpcException extends RuntimeException {

    public RpcException() {
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
