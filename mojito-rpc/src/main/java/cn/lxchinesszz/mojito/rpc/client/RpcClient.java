package cn.lxchinesszz.mojito.rpc.client;


/**
 * @author liuxin
 * 2022/9/11 23:52
 */
public interface RpcClient {

//    private final ServerDiscover<Object> serverDiscover;
//
//    public RpcClient(ServerDiscover<Object> serverDiscover) {
//        this.serverDiscover = serverDiscover;
//    }
//
//    public void client(Class<?>... interfaceTypes) {
//        serverDiscover
//    }

//    /**
//     * 注册一个接口服务
//     *
//     * @param interfaceType
//     */
//    void registerInterface(Class<?> interfaceType);

    /**
     * 获取接口服务
     *
     * @param interfaceType 接口类型
     * @param <T>           接口泛型
     * @return T
     */
    <T> T getObject(Class<T> interfaceType);
}
