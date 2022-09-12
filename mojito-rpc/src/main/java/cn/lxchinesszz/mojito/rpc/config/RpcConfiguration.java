package cn.lxchinesszz.mojito.rpc.config;

import cn.lxchinesszz.mojito.rpc.directory.ServerDiscover;
import cn.lxchinesszz.mojito.rpc.directory.ServiceRegister;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.MojitoInvoker;

/**
 * @author liuxin
 * 2022/9/12 20:47
 */
public class RpcConfiguration {

    private ServerDiscover serverDiscover;

    private ServiceRegister serviceRegister;

    /**
     * 获取服务端配置
     *
     * @param service 服务
     * @param <T>     泛型
     * @return Invoker<T>
     */
    public <T> Invoker<T> getInvoker(Object service) {
        return new MojitoInvoker<>(service);
    }

    public <T> Invoker<T> getClusterInvoker() {
        return null;
    }
}
