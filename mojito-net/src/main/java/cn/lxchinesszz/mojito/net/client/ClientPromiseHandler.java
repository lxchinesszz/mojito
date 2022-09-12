package cn.lxchinesszz.mojito.net.client;

import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.net.future.MojitoFuture;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

/**
 * @author liuxin
 * 2020-08-01 19:51
 */
public interface ClientPromiseHandler<T extends ProtocolHeader, R extends ProtocolHeader> {

    /**
     * 异步发送请求
     *
     * @param enhanceChannel 客户端和服务端建立的连接通道
     * @param rpcRequest     客户端发送的请求信息
     * @return MojitoFuture
     */
    MojitoFuture<R> sendAsync(EnhanceChannel enhanceChannel, T rpcRequest);

    /**
     * 接受服务端的响应
     *
     * @param rpcResponse 服务端的响应信息
     */
    void received(R rpcResponse);


}
