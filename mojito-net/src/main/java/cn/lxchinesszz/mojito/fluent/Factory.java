package cn.lxchinesszz.mojito.fluent;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.client.Client;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.server.Server;


/**
 * @author liuxin
 * 2020-08-22 14:32
 */
public interface Factory<REQ extends ProtocolHeader, RES extends ProtocolHeader> extends ConfigurableFactory<REQ, RES>, Protocol<REQ, RES> {

    /**
     * 通信的协议信息
     *
     * @return Protocol
     */
    Protocol<REQ, RES> getProtocol();

    /**
     * 服务端实例
     *
     * @return Server
     */
    Server<?> getServer();

    /**
     * 客户端实例
     *
     * @return Client
     */
    Client<REQ, RES> getClient();

    /**
     * 客户端实例
     *
     * @param remoteHost 连接ip
     * @param remotePort 连接端口
     * @return Client
     * @throws Exception 未知异常
     */
    Client<REQ, RES> getClient(String remoteHost, int remotePort) throws Exception;

    /**
     * 设置服务端的处理器
     * 目的将请求信息通过处理,生成返回值
     *
     * @param businessHandler 服务端处理器
     */
    void setBusinessHandler(BusinessHandler<REQ, RES> businessHandler);

    /**
     * 设置客户端通知处理器
     * 通道是长连接,该处理器目的是保证在长连接下,数据的一一对应关系
     *
     * @param clientPromiseHandler 客户处理器
     */
    void setClientPromiseHandler(ClientPromiseHandler<REQ, RES> clientPromiseHandler);

}
