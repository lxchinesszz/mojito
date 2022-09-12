package cn.lxchinesszz.mojito.net.client;


import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

/**
 * 服务器初始化
 *
 * @author liuxin
 * 2022/8/6 13:30
 */
public interface ClientInitializer<T extends Client<? extends ProtocolHeader, ? extends ProtocolHeader>> {
    void initializer(T client);
}
