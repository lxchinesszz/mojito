package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * @version Id: RemotingException.java, v 0.1 2019-05-11 14:48
 */
public class ProtocolException extends RuntimeException {
    public ProtocolException() {
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }

    public ProtocolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
