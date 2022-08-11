package cn.lxchinesszz.mojito.client;

import cn.lxchinesszz.mojito.future.MojitoFuture;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

/**
 * @author liuxin
 * 2022/8/9 20:41
 */
public interface Client<REQ extends ProtocolHeader, RES extends ProtocolHeader> extends ConfigurableClient<REQ, RES, Client<REQ, RES>> {


    /**
     * 建立连接
     *
     * @param host 连接地址
     * @param port 连接端口
     */
    void connect(String host, Integer port);

    /**
     * 发送消息
     *
     * @param req 请求体
     * @return 异步结果
     */
    MojitoFuture<RES> send(REQ req);

    /**
     * 关闭连接
     */
    void close();


    /**
     * 是否连接
     *
     * @return boolean
     */
    boolean isRun();

    /**
     * 是否连接
     *
     * @return boolean
     */
    boolean isConnected();


}
