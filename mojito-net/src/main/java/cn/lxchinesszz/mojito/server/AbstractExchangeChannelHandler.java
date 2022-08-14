package cn.lxchinesszz.mojito.server;

import cn.lxchinesszz.mojito.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.exception.RemotingException;
import cn.lxchinesszz.mojito.exchange.ExchangeChannelHandler;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

/**
 * @author liuxin
 * 2022/8/8 21:35
 */
public abstract class AbstractExchangeChannelHandler implements ExchangeChannelHandler {

    protected boolean isServer;

    protected Protocol<? extends ProtocolHeader,? extends ProtocolHeader> protocol;

    public AbstractExchangeChannelHandler(boolean isServer, Protocol<? extends ProtocolHeader, ? extends ProtocolHeader> protocol) {
        this.isServer = isServer;
        this.protocol = protocol;
    }

    @Override
    public void connected(EnhanceChannel channel) throws RemotingException {

    }

    @Override
    public void disconnected(EnhanceChannel channel) throws RemotingException {

    }

    @Override
    public void write(EnhanceChannel channel, Object message) throws RemotingException {

    }

    @Override
    public void read(EnhanceChannel channel, Object message) throws RemotingException {

    }

    @Override
    public void caught(EnhanceChannel channel, Throwable exception) throws RemotingException {

    }
}
