package cn.lxchinesszz.mojito.net.task;

import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.net.channel.context.DefaultChannelContext;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

import java.util.Map;

/**
 * @author liuxin
 * 2020-09-14 17:52
 */
public class RpcServerHandlerTask extends AbstractHandlerTask<ProtocolHeader, ProtocolHeader> {

    public RpcServerHandlerTask(BusinessHandler<ProtocolHeader, ProtocolHeader> serverHandler, EnhanceChannel enhanceChannel, ProtocolHeader request) {
        super(serverHandler, enhanceChannel, request);
    }

    @Override
    public ProtocolHeader doResult() {
        ProtocolHeader rpcRequest = getRequest();
        Map<String, String> attachments = rpcRequest.getAttachments();
        EnhanceChannel enhanceChannel = getEnhanceChannel();
        for (Map.Entry<String, String> entry : attachments.entrySet()) {
            enhanceChannel.setAttribute(entry.getKey(), entry.getValue());
        }
        ProtocolHeader response;
        try {
            response = getBusinessHandler().handler(new DefaultChannelContext(getEnhanceChannel()), getRequest());
            response.setId(rpcRequest.getId());
            response.setProtocolType(rpcRequest.getProtocolType());
            response.setSerializationType(rpcRequest.getSerializationType());
        } catch (Exception e) {
            response = new ProtocolHeader();
            response.setId(rpcRequest.getId());
            response.setProtocolType(rpcRequest.getProtocolType());
            response.setSerializationType(rpcRequest.getSerializationType());
            response.setException(e);
        }
        return response;
    }
}

