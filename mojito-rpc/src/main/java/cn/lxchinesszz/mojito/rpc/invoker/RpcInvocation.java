package cn.lxchinesszz.mojito.rpc.invoker;

import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import lombok.Builder;

import java.io.Serializable;

/**
 * @author liuxin
 * 2022/9/7 20:15
 */
@Builder
public class RpcInvocation extends ProtocolHeader implements Invocation, Serializable {

    private String methodName;

    private Class<?> interfaceType;

    private Object[] arguments;

    @Override
    public String getMethodName() {
        return methodName;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Class<?> getInterface() {
        return interfaceType;
    }

    @Override
    public String getAttachment(String key, String defaultValue) {
        return super.getAttachment(key, defaultValue);
    }
}
