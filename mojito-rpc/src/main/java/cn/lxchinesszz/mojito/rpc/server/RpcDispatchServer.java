package cn.lxchinesszz.mojito.rpc.server;

import cn.lxchinesszz.mojito.net.business.BusinessHandler;
import cn.lxchinesszz.mojito.net.channel.context.ChannelContext;
import cn.lxchinesszz.mojito.net.exception.RemotingException;
import cn.lxchinesszz.mojito.net.fluent.Mojito;
import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import cn.lxchinesszz.mojito.rpc.invoker.RpcResult;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author liuxin
 * 2022/9/7 22:37
 */
public class RpcDispatchServer {

    private Integer port;

    private final ServerDiscover serverDiscover;

    public <T> RpcDispatchServer(ServerDiscover serverDiscover) {
        this.serverDiscover = serverDiscover;
    }

    public void start(Integer port) {
        this.port = port;
        Mojito.server(RpcInvocation.class, RpcResult.class).businessHandler(new BusinessHandler<RpcInvocation, RpcResult>() {
            @Override
            @SuppressWarnings("all")
            public RpcResult handler(ChannelContext channelContext, RpcInvocation request) throws RemotingException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
                List<Invoker<Object>> list = serverDiscover.list(request);
                return (RpcResult) list.get(0).invoke(request);
            }
        }).create().startAsync(8080);
    }
}
