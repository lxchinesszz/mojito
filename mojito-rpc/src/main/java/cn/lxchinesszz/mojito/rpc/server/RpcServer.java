package cn.lxchinesszz.mojito.rpc.server;

import cn.lxchinesszz.mojito.rpc.directory.ServiceCenter;

/**
 * @author liuxin
 * 2022/9/12 20:07
 */
public interface RpcServer {

    void export(Object service);

    ServiceCenter getServiceCenter();

    void start(Integer port);

    void startAsync(Integer port);
}
