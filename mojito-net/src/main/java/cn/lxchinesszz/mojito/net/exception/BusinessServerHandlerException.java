package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * 2020-09-03 22:37
 */
public class BusinessServerHandlerException extends RuntimeException {


    public BusinessServerHandlerException() {
    }

    public BusinessServerHandlerException(String message) {
        super(message);
    }

    public BusinessServerHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessServerHandlerException(Throwable cause) {
        super(cause);
    }

    public BusinessServerHandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}