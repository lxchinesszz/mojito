package cn.lxchinesszz.mojito.net.exchange;

import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.net.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.net.exception.RemotingException;
import cn.lxchinesszz.mojito.net.protocol.Protocol;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.net.server.AbstractExchangeChannelHandler;
import cn.lxchinesszz.mojito.net.task.HandlerTask;
import cn.lxchinesszz.mojito.net.task.RpcClientHandlerTask;
import cn.lxchinesszz.mojito.net.task.RpcServerHandlerTask;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * 自定义是为了给原生API,进行包装,方便扩展
 *
 * @author liuxin
 * 2022/8/8 21:40
 */
public class DefaultExchangeChannelHandler extends AbstractExchangeChannelHandler {


    public DefaultExchangeChannelHandler(boolean isServer, Protocol<? extends ProtocolHeader, ? extends ProtocolHeader> protocol) {
        super(isServer, protocol);
    }

    @Override
    public void connected(EnhanceChannel channel) throws RemotingException {
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void read(EnhanceChannel channel, Object message) throws RemotingException {
        if (isServer) {
            BusinessHandler businessHandler = protocol.getBusinessHandler();
            if (protocol.acceptInboundMessage(message)) {
                HandlerTask handlerTask;
                if (message instanceof FullHttpRequest) {
                    // HTTP协议
                }
                if (message instanceof ProtocolHeader) {
                    // RPC 协议
                    handlerTask = new RpcServerHandlerTask(businessHandler, channel, (ProtocolHeader) message);
                    Executor executor = protocol.getExecutor();
                    if (Objects.isNull(executor)) {
                        handlerTask.justStart();
                    } else {
                        protocol.getExecutor().execute(handlerTask);
                    }
                }
            }
        } else {
            ClientPromiseHandler clientPromiseHandler = protocol.getClientPromiseHandler();
            HandlerTask handlerTask;
            if (message instanceof FullHttpRequest) {
                // HTTP协议
            }
            if (message instanceof ProtocolHeader) {
                // RPC 协议
                handlerTask = new RpcClientHandlerTask(clientPromiseHandler, channel, (ProtocolHeader) message);
                Executor executor = protocol.getExecutor();
                if (Objects.isNull(executor)) {
                    handlerTask.justStart();
                } else {
                    protocol.getExecutor().execute(handlerTask);
                }
            }
        }

    }


    @Override
    public void disconnected(EnhanceChannel channel) throws RemotingException {
    }

}
