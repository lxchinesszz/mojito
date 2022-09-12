package cn.lxchinesszz.mojito.net.exception;

/**
 * @author liuxin
 * 2020-09-14 19:41
 */
public class HttpsTokenFileException extends RuntimeException {

    public HttpsTokenFileException() {
    }

    public HttpsTokenFileException(String s) {
        super(s);
    }

    public HttpsTokenFileException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public HttpsTokenFileException(Throwable throwable) {
        super(throwable);
    }

    public HttpsTokenFileException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
