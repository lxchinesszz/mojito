package cn.lxchinesszz.mojito.net.channel;

import java.net.InetSocketAddress;

/**
 * @author liuxin
 * @version Id: Endpoint.java, v 0.1 2019-05-11 10:54
 */
public interface Endpoint {

    /**
     * 端点地址
     *
     * @return 网络地址
     */
    InetSocketAddress getLocalAddress();

}
