package cn.lxchinesszz.mojito.fluent;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

/**
 * ConfigurableFactory
 *
 * @author liuxin
 * 2020-08-23 21:10
 */
public interface ConfigurableFactory<REQ extends ProtocolHeader, RES extends ProtocolHeader> {

    /**
     * 服务端处理逻辑
     *
     * @param serverHandler 服务端处理逻辑
     */
    void setServerHandler(BusinessHandler<REQ, RES> serverHandler);

    /**
     * 客户端处理逻辑
     *
     * @param clientPromiseHandler 客户端处理器
     */
    void setClientPromiseHandler(ClientPromiseHandler<REQ, RES> clientPromiseHandler);

}
