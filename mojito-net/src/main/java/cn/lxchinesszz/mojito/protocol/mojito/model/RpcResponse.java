package cn.lxchinesszz.mojito.protocol.mojito.model;

import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Arrays;

/**
 * @author liuxin
 * 2020-07-31 19:40
 */
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
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
    private Class<?> returnType;

    /**
     * 超时时间
     */
    private long timeout;
}
