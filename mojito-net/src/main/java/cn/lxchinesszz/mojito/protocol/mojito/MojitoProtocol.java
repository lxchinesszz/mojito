package cn.lxchinesszz.mojito.protocol.mojito;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.client.DefaultClientPromiseHandler;
import cn.lxchinesszz.mojito.codec.ChannelDecoder;
import cn.lxchinesszz.mojito.codec.ChannelEncoder;
import cn.lxchinesszz.mojito.codec.MojitoChannelDecoder;
import cn.lxchinesszz.mojito.codec.MojitoChannelEncoder;
import cn.lxchinesszz.mojito.protocol.AbstractProtocol;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.mojito.model.RpcRequest;
import cn.lxchinesszz.mojito.protocol.mojito.model.RpcResponse;
import cn.lxchinesszz.mojito.server.DefaultExchangeChannelHandler;
import cn.lxchinesszz.mojito.server.ExchangeChannelHandler;

import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2022/8/6 14:57
 */
public class MojitoProtocol extends AbstractProtocol<RpcRequest, RpcResponse> {


    public MojitoProtocol(BusinessHandler<RpcRequest, RpcResponse> businessHandler) {
        super(businessHandler);
    }

    @Override
    public String name() {
        return "Mojito";
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public ChannelDecoder getRequestDecoder() {
        return new MojitoChannelDecoder();
    }

    @Override
    public ChannelEncoder<?> getResponseEncoder() {
        return new MojitoChannelEncoder();
    }

}
