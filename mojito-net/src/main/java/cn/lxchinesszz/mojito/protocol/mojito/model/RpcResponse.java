package cn.lxchinesszz.mojito.protocol.mojito.model;

import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

import java.util.Arrays;

/**
 * @author liuxin
 * 2020-07-31 19:40
 */
public class RpcResponse extends ProtocolHeader {


    /**
     * 服务类型
     */
    private Class<?> serviceType;

    /**
     * 请求方法
     */
    private String methodName;

    /**
     * 参数类型
     */
    private Class<?>[] argsType;

    /**
     * 返回值
     */
    private Object result;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 返回值类型
     */
    private Class returnType;

    /**
     * 超时时间
     */
    private long timeout;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Class<?> getServiceType() {
        return serviceType;
    }

    public void setServiceType(Class<?> serviceType) {
        this.serviceType = serviceType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getArgsType() {
        return argsType;
    }

    public void setArgsType(Class<?>[] argsType) {
        this.argsType = argsType;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
        setSuccess(true);
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "RpcResponse{" +
                ", timeout=" + timeout +
                ", serviceType=" + serviceType +
                ", methodName='" + methodName + '\'' +
                ", argsType=" + Arrays.toString(argsType) +
                ", result=" + result +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", code=" + code +
                ", returnType=" + returnType +
                '}';
    }
}
