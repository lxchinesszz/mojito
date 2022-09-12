package cn.lxchinesszz.mojito.net.fluent;

import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.net.codec.ChannelDecoder;
import cn.lxchinesszz.mojito.net.codec.ChannelEncoder;
import cn.lxchinesszz.mojito.net.exchange.DefaultExchangeChannelHandler;
import cn.lxchinesszz.mojito.net.protocol.Protocol;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.net.exchange.ExchangeChannelHandler;

import java.util.concurrent.Executor;


/**
 * 1. 自己定义数据模型和服务端和客户端的处理逻辑(数据模型只要继承RpcProtocolHeader即可)
 * 2. 编码器和解码器使用默认即可,已经实现拆包和粘包问题,所以不用担心该问题
 *
 * @author liuxin
 * 2020-08-22 13:27
 */
public abstract class AbstractFactory<REQ extends ProtocolHeader, RES extends ProtocolHeader> implements Factory<REQ, RES> {

    /**
     * 服务端使用
     */
    private BusinessHandler<REQ, RES> businessHandler;

    /**
     * 客户端使用
     */
    private ClientPromiseHandler<REQ, RES> clientPromiseHandler;

    /**
     * 协议贯穿
     */
    private Protocol<REQ, RES> protocol;

    public AbstractFactory(Protocol<REQ, RES> protocol) {
        this.protocol = protocol;
    }

    public AbstractFactory(Protocol<REQ, RES> protocol, BusinessHandler<REQ, RES> businessHandler) {
        this.protocol = protocol;
        this.businessHandler = businessHandler;
    }

    @Override
    public String name() {
        return protocol.name();
    }

    @Override
    public Protocol<REQ, RES> getProtocol() {
        return protocol;
    }

    public ExchangeChannelHandler getExchangeChannelHandler() {
        return new DefaultExchangeChannelHandler(true, getProtocol());
    }

    @Override
    public Executor getExecutor() {
        return getProtocol().getExecutor();
    }

    @Override
    public ChannelDecoder getRequestDecoder() {
        return protocol.getRequestDecoder();
    }

    @Override
    public ChannelEncoder getResponseEncoder() {
        return protocol.getResponseEncoder();
    }

    @Override
    public void setServerHandler(BusinessHandler<REQ, RES> serverHandler) {

    }

    @Override
    public void setBusinessHandler(BusinessHandler<REQ, RES> businessHandler) {

    }

    @Override
    public void setClientPromiseHandler(ClientPromiseHandler<REQ, RES> clientPromiseHandler) {

    }

    @Override
    public BusinessHandler<REQ, RES> getBusinessHandler() {
        return businessHandler;
    }

    @Override
    public ClientPromiseHandler<REQ, RES> getClientPromiseHandler() {
        return clientPromiseHandler;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) {
        return false;
    }
}
