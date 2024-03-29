package cn.lxchinesszz.mojito.net.protocol;

import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.net.codec.ChannelDecoder;
import cn.lxchinesszz.mojito.net.codec.ChannelEncoder;

import java.util.concurrent.Executor;

/**
 * 协议的组成部分
 * 协议解码器 + 协议处理器 + 协议处理器
 *
 * @author liuxin
 * 2020-07-25 21:39
 */
public interface Protocol<R extends ProtocolHeader, V extends ProtocolHeader> {

    /**
     * 协议名称
     *
     * @return String
     */
    String name();

    /**
     * 可以指定处理器,如果不指定默认使用netty work线程
     *
     * @return Executor
     */
    Executor getExecutor();

    /**
     * 请求解码器
     *
     * @return ChannelDecoder
     */
    ChannelDecoder getRequestDecoder();

    /**
     * 响应编码器
     *
     * @return ChannelEncoder
     */
    ChannelEncoder<?> getResponseEncoder();

    void setBusinessHandler(BusinessHandler<R, V> businessHandler);

    BusinessHandler<R, V> getBusinessHandler();

    ClientPromiseHandler<R, V> getClientPromiseHandler();

    boolean acceptInboundMessage(Object msg);

}
