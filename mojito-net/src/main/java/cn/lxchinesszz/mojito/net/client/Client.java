package cn.lxchinesszz.mojito.net.client;

import cn.lxchinesszz.mojito.net.future.MojitoFuture;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

import java.util.concurrent.ExecutionException;

/**
 * @author liuxin
 * 个人博客：https://java.springlearn.cn
 * 公众号：西魏陶渊明  ｛关注获取学习源码｝
 * 2022/8/5 23:12
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
    MojitoFuture<RES> sendAsync(REQ req);

    RES send(REQ req) throws InterruptedException, ExecutionException;

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
