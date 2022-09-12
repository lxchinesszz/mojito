package cn.lxchinesszz.mojito.fluent;

import cn.lxchinesszz.mojito.net.client.Client;
import cn.lxchinesszz.mojito.net.fluent.Mojito;
import cn.lxchinesszz.mojito.net.future.MojitoFuture;
import cn.lxchinesszz.mojito.net.future.listener.MojitoListener;
import cn.lxchinesszz.mojito.net.protocol.mojito.model.RpcRequest;
import cn.lxchinesszz.mojito.net.protocol.mojito.model.RpcResponse;
import cn.lxchinesszz.mojito.net.server.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * @author liuxin
 * 2022/8/11 20:55
 */
class MojitoTest {

    /**
     * @author liuxin
     * 个人博客：https://java.springlearn.cn
     * 公众号：西魏陶渊明  ｛关注获取学习源码｝
     * 2022/8/11 23:12
     */
    @Test
    @DisplayName("构建客户端【异步方式】")
    public void clientAsync() throws Exception {
        // 构建连接
        Client<RpcRequest, RpcResponse> client = Mojito.client(RpcRequest.class, RpcResponse.class)
                .connect("127.0.0.1", 6666);

        MojitoFuture<RpcResponse> sendFuture = client.sendAsync(new RpcRequest());
        sendFuture.addListeners(new MojitoListener<RpcResponse>() {
            @Override
            public void onSuccess(RpcResponse result) {
                System.out.println("收到结果:" + result);
            }

            @Override
            public void onThrowable(Throwable throwable) {
                System.err.println("处理失败:" + throwable.getMessage());
            }
        });
        Thread.currentThread().join();
    }

    /**
     * @author liuxin
     * 个人博客：https://java.springlearn.cn
     * 公众号：西魏陶渊明  ｛关注获取学习源码｝
     * 2022/8/11 23:12
     */
    @Test
    @DisplayName("构建客户端【同步方式】")
    public void clientSync() throws Exception{
        Client<RpcRequest, RpcResponse> client = Mojito.client(RpcRequest.class, RpcResponse.class)
                .connect("127.0.0.1", 6666);
        System.out.println(client.send(new RpcRequest()));
    }

    /**
     * @author liuxin
     * 个人博客：https://java.springlearn.cn
     * 公众号：西魏陶渊明  ｛关注获取学习源码｝
     * 2022/8/11 23:12
     */
    @Test
    @DisplayName("构建服务端【阻塞方式】")
    public void serverSync() throws Exception {
        Server<?> server = Mojito.server(RpcRequest.class, RpcResponse.class)
                // 业务层,读取请求对象,返回结果
                .businessHandler((channelContext, request) -> new RpcResponse())
                .create();
        server.start(6666);
    }

    /**
     * @author liuxin
     * 个人博客：https://java.springlearn.cn
     * 公众号：西魏陶渊明  ｛关注获取学习源码｝
     * 2022/8/11 23:12
     */
    @Test
    @DisplayName("构建服务端【非阻塞方式】")
    public void serverAsync() throws Exception {
        Server<?> server = Mojito.server(RpcRequest.class, RpcResponse.class)
                // 业务层,读取请求对象,返回结果
                .businessHandler((channelContext, request) -> new RpcResponse())
                .create();
        server.startAsync(6666);
    }
}