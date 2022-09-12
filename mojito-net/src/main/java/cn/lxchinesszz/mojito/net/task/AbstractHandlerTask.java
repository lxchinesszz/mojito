package cn.lxchinesszz.mojito.net.task;


import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.net.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.net.exception.RemotingException;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxin
 * 2020-09-14 17:43
 */
@Slf4j
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
        return Boolean.parseBoolean(getRequest().getAttachment(KEEPALIVE));
//        return Boolean.parseBoolean(String.valueOf(getEnhanceChannel().getAttribute(KEEPALIVE)));
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
                // 默认都是长连接
                if (keepAlive()) {
                    enhanceChannel.send(v);
                    log.info("RequestId:{},【长连接】服务端发送数据", getRequest().getId());
                } else {
                    enhanceChannel.sendAndClose(v);
                    log.info("RequestId:{},【短连接】服务端发送数据", getRequest().getId());
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
