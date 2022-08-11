package cn.lxchinesszz.mojito.client;


import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

/**
 * 服务器初始化
 *
 * @author liuxin
 * 2022/8/6 13:30
 */
public interface ClientInitializer<T extends Client<? extends ProtocolHeader, ? extends ProtocolHeader>> {
    void initializer(T client);
}
