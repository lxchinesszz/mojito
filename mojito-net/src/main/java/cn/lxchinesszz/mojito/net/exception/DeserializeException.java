package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * @version Id: RemotingException.java, v 0.1 2019-05-11 14:48
 */
public class DeserializeException extends RuntimeException {
    public DeserializeException() {
    }

    public DeserializeException(String message) {
        super(message);
    }

    public DeserializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeserializeException(Throwable cause) {
        super(cause);
    }

    public DeserializeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
