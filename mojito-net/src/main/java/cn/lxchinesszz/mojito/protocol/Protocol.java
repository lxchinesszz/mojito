package cn.lxchinesszz.mojito.protocol;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.codec.ChannelDecoder;
import cn.lxchinesszz.mojito.codec.ChannelEncoder;
import cn.lxchinesszz.mojito.server.ExchangeChannelHandler;

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

    BusinessHandler<R, V> getBusinessHandler();

    ClientPromiseHandler<R,V> getClientPromiseHandler();

    boolean acceptInboundMessage(Object msg);

}
