package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * @version Id: RemotingException.java, v 0.1 2019-05-11 14:48
 */
public class SerializeException extends RuntimeException {
    public SerializeException() {
    }

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }

    public SerializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
