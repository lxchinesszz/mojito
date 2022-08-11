package cn.lxchinesszz.mojito.protocol.mojito.model;

import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.serialize.Serializer;

import java.io.Serializable;
import java.util.Arrays;

/**
 * TODO是否可以带来身份,根据身份来给服务地方来判断？
 *
 * @author liuxin
 * 2020-07-31 19:40
 */
public class RpcRequest extends ProtocolHeader implements Serializable {


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
    private Class[] argsType;

    /**
     * 返回值类型
     */
    private Class returnType;

    /**
     * 版本号
     */
    private String version;

    /**
     * 请求参数
     */
    private Object[] args;

    /**
     * 超时时间
     */
    private long timeout;

    public RpcRequest() {
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

    public Class[] getArgsType() {
        return argsType;
    }

    public void setArgsType(Class[] argsType) {
        this.argsType = argsType;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
//                " id=" + id +
//                ", protocolType=" + protocolType +
//                ", serializationType=" + serializationType +
//                ", type=" + type +
                ", timeout=" + timeout +
                ", serviceType=" + serviceType +
                ", methodName='" + methodName + '\'' +
                ", argsType=" + Arrays.toString(argsType) +
                ", returnType=" + returnType +
                ", version='" + version + '\'' +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
