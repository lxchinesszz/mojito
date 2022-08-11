package cn.lxchinesszz.mojito.task;

import cn.lxchinesszz.mojito.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

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
