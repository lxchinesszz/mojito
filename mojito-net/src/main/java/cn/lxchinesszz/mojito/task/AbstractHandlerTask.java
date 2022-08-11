package cn.lxchinesszz.mojito.task;


import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.exception.RemotingException;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

/**
 * @author liuxin
 * 2020-09-14 17:43
 */
public abstract class AbstractHandlerTask<R extends ProtocolHeader, V extends ProtocolHeader> implements HandlerTask {

    public static final String KEEPALIVE = "keep-alive";

    private BusinessHandler<R, V> businessHandler;

    private ClientPromiseHandler<R, V> clientPromiseHandler;

    private EnhanceChannel enhanceChannel;

    private R request;

    public AbstractHandlerTask(ClientPromiseHandler<R, V> clientPromiseHandler, EnhanceChannel enhanceChannel, R request) {
        this.clientPromiseHandler = clientPromiseHandler;
        this.enhanceChannel = enhanceChannel;
        this.request = request;
    }

    public AbstractHandlerTask(BusinessHandler<R, V> serverHandler, EnhanceChannel enhanceChannel, R request) {
        this.businessHandler = serverHandler;
        this.enhanceChannel = enhanceChannel;
        this.request = request;
    }

    public ClientPromiseHandler<R, V> getClientPromiseHandler() {
        return clientPromiseHandler;
    }

    public EnhanceChannel getEnhanceChannel() {
        return enhanceChannel;
    }


    public R getRequest() {
        return request;
    }

    public boolean keepAlive() {
        return Boolean.parseBoolean(String.valueOf(getEnhanceChannel().getAttribute(KEEPALIVE)));
    }

    @Override
    public void justStart() {
        run();
    }

    @Override
    public void run() {
        try {
            V v = doResult();
            if (this instanceof RpcClientHandlerTask) {
                return;
            }
            // 服务端也可以对通道进行写,但是写完之后要将标记置位不可写。否则这里会在写一次
            EnhanceChannel enhanceChannel = getEnhanceChannel();
            if (enhanceChannel.isWrite()) {
                if (keepAlive()) {
                    enhanceChannel.send(v);
                } else {
                    enhanceChannel.sendAndClose(v);
                }
            } else {
                //重置可写
                enhanceChannel.markWrite();
            }
        } catch (Throwable throwable) {
            enhanceChannel.exceptionCaught(new RemotingException(throwable));
            enhanceChannel.disconnected();
        }
    }

    public BusinessHandler<R, V> getBusinessHandler() {
        return businessHandler;
    }

    public abstract V doResult();
}
