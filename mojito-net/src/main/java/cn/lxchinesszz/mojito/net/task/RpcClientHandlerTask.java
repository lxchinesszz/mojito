package cn.lxchinesszz.mojito.net.task;

import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.net.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

/**
 * @author liuxin
 * 2020-09-14 18:12
 */
public class RpcClientHandlerTask extends AbstractHandlerTask<ProtocolHeader, ProtocolHeader> {

    public RpcClientHandlerTask(ClientPromiseHandler<ProtocolHeader, ProtocolHeader> clientPromiseHandler, EnhanceChannel enhanceChannel, ProtocolHeader request) {
        super(clientPromiseHandler, enhanceChannel, request);
    }


    @Override
    public ProtocolHeader doResult() {
        getClientPromiseHandler().received(getRequest());
        return null;
    }
}
