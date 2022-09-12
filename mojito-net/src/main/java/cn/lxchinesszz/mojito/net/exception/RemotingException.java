package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * @version Id: RemotingException.java, v 0.1 2019-05-11 14:48
 */
public class RemotingException extends RuntimeException {
    public RemotingException() {
    }

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingException(Throwable cause) {
        super(cause);
    }

    public RemotingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
