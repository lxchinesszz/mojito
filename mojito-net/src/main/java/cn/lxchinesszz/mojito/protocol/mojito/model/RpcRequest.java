package cn.lxchinesszz.mojito.protocol.mojito.model;

import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.serialize.Serializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;

/**
 * TODO是否可以带来身份,根据身份来给服务地方来判断？
 *
 * @author liuxin
 * 2020-07-31 19:40
 */
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
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
    private Class<?>[] argsType;

    /**
     * 返回值类型
     */
    private Class<?> returnType;

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
}
