package cn.lxchinesszz.mojito.net.protocol.mojito;

import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.codec.ChannelDecoder;
import cn.lxchinesszz.mojito.net.codec.ChannelEncoder;
import cn.lxchinesszz.mojito.net.codec.MojitoChannelDecoder;
import cn.lxchinesszz.mojito.net.codec.MojitoChannelEncoder;
import cn.lxchinesszz.mojito.net.protocol.AbstractProtocol;
import cn.lxchinesszz.mojito.net.protocol.mojito.model.RpcRequest;
import cn.lxchinesszz.mojito.net.protocol.mojito.model.RpcResponse;

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
