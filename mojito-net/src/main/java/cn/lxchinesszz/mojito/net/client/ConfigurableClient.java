package cn.lxchinesszz.mojito.net.client;

import cn.lxchinesszz.mojito.net.protocol.Protocol;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

/**
 * @author liuxin
 * 个人博客：https://java.springlearn.cn
 * 公众号：西魏陶渊明  ｛关注获取学习源码｝
 * 2022/8/5 23:12
 */
public interface ConfigurableClient<REQ extends ProtocolHeader, RES extends ProtocolHeader, T extends Client<REQ, RES>> {

    /**
     * 给网络通道注册二进制处理协议
     *
     * @param protocol 协议
     */
    void registryProtocol(Protocol<REQ, RES> protocol);

    /**
     * 协议信息
     *
     * @return Protocol
     */
    Protocol<REQ, RES> getProtocol();

    /**
     * 注册钩子程序
     */
    void registryHooks(Runnable hookTask);

    /**
     * 这里我们提供一个Server初始化的方法,为什么呢?
     * 目前我们的服务端是使用NettyServer,我们也支持其他的通信框架。因为可能初始化方法不一样.
     * 所以我们将具体的实现作为一个泛型。让具体的实现来自己定义自己的初始化方法
     *
     * @param initializer 初始化接口
     */
    void initializer(ClientInitializer initializer);

    /**
     * 客户端初始化扩展
     *
     * @return ClientInitializer
     */
    ClientInitializer<Client<REQ, RES>> getClientInitializer();

}
