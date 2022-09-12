package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * @version Id: RemotingException.java, v 0.1 2019-05-11 14:48
 */
public class RemoteConnectException extends RuntimeException {
    public RemoteConnectException() {
    }

    public RemoteConnectException(String message) {
        super(message);
    }

    public RemoteConnectException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteConnectException(Throwable cause) {
        super(cause);
    }

    public RemoteConnectException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
