package cn.lxchinesszz.mojito.rpc.exeception;

/**
 * @author liuxin
 * 2022/8/28 23:35
 */
public class ReflectionException extends RuntimeException{

    public ReflectionException() {
        super();
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }
}
