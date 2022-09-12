package cn.lxchinesszz.mojito.rpc.server.impl;

import cn.lxchinesszz.mojito.net.fluent.Mojito;
import cn.lxchinesszz.mojito.net.server.Server;
import cn.lxchinesszz.mojito.rpc.directory.ServiceCenter;
import cn.lxchinesszz.mojito.rpc.directory.impl.LocalServiceCenter;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import cn.lxchinesszz.mojito.rpc.invoker.RpcResult;
import cn.lxchinesszz.mojito.rpc.server.RpcServer;

import java.util.List;


/**
 * @author liuxin
 * 2022/9/12 20:09
 */
public class DefaultRpcServer implements RpcServer {

    private final Server<?> server;

    private final ServiceCenter serviceCenter;


    public DefaultRpcServer(ServiceCenter serviceCenter) {
        this.serviceCenter = serviceCenter;
        this.server = Mojito.server(RpcInvocation.class, RpcResult.class).businessHandler(
                (channelContext, request) -> {
                    List<Invoker<Object>> list = serviceCenter.list(request);
                    return (RpcResult) list.get(0).invoke(request);
                }
        ).create();
    }


    public ServiceCenter getServiceCenter() {
        return serviceCenter;
    }

    @Override
    public void export(Object service) {
        serviceCenter.registerService(service);
    }

    @Override
    public void start(Integer port) {
        server.start(port);
    }

    @Override
    public void startAsync(Integer port) {
        server.startAsync(port);
    }
}
