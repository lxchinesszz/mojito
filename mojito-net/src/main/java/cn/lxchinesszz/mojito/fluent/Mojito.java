package cn.lxchinesszz.mojito.fluent;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.client.Client;
import cn.lxchinesszz.mojito.client.netty.NettyClient;
import cn.lxchinesszz.mojito.client.netty.NettyClientInitializer;
import cn.lxchinesszz.mojito.codec.ChannelDecoder;
import cn.lxchinesszz.mojito.codec.ChannelEncoder;
import cn.lxchinesszz.mojito.codec.MojitoChannelDecoder;
import cn.lxchinesszz.mojito.codec.MojitoChannelEncoder;
import cn.lxchinesszz.mojito.protocol.AbstractProtocol;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.server.Server;
import cn.lxchinesszz.mojito.server.netty.NettyServer;
import cn.lxchinesszz.mojito.server.netty.NettyServerInitializer;

import java.util.concurrent.Executor;

/**
 * @author liuxin
 * 2022/8/11 20:54
 */
public class Mojito<REQ extends ProtocolHeader, RES extends ProtocolHeader> {

    private final Factory<REQ, RES> codecFactory;

    private Class<REQ> requestType;

    private Class<RES> responseType;

    private Mojito(Factory<REQ, RES> codecFactory) {
        this.codecFactory = codecFactory;
    }


    public void setRequestType(Class<REQ> requestType) {
        this.requestType = requestType;
    }

    public void setResponseType(Class<RES> responseType) {
        this.responseType = responseType;
    }

    public Factory<REQ, RES> getCodecFactory() {
        return codecFactory;
    }

    public Class<REQ> getRequestType() {
        return requestType;
    }

    public Class<RES> getResponseType() {
        return responseType;
    }

    /**
     * 核心组件
     *
     * @param requestType  请求类型
     * @param responseType 响应类型
     * @param <REQ>        请求泛型
     * @param <RES>        响应泛型
     * @return Mojito
     */
    private static <REQ extends ProtocolHeader, RES extends ProtocolHeader> Mojito<REQ, RES> module(Class<REQ> requestType, Class<RES> responseType) {
        Protocol<REQ, RES> customerModelProtocol = new AbstractProtocol<REQ, RES>() {
            @Override
            public String name() {
                return "mojito";
            }

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public ChannelDecoder getRequestDecoder() {
                return new MojitoChannelDecoder();
            }

            @Override
            public ChannelEncoder<?> getResponseEncoder() {
                return new MojitoChannelEncoder();
            }
        };

        Factory<REQ, RES> codecFactory = new AbstractFactory<REQ, RES>(customerModelProtocol) {

            @Override
            public Client<REQ, RES> getClient() {
                Client<REQ, RES> client = new NettyClient<>();
                client.registryProtocol(getProtocol());
                client.initializer(new NettyClientInitializer());
                return client;
            }

            @Override
            public Server<?> getServer() {
                NettyServer nettyServer = new NettyServer();
                nettyServer.registryProtocol(getProtocol());
                nettyServer.initializer(new NettyServerInitializer());
                return nettyServer;
            }

            @Override
            public Client<REQ, RES> getClient(String remoteHost, int remotePort) throws Exception {
                getClient().connect(remoteHost, remotePort);
                return getClient();
            }
        };

        Mojito<REQ, RES> mojito = new Mojito<>(codecFactory);
        mojito.setRequestType(requestType);
        mojito.setResponseType(responseType);
        return mojito;
    }


    /**
     * 使用该方法需要注意如果是服务端,一定要调用serverHandler方法创建
     *
     * @param requestType  定义的请求数据类型
     * @param responseType 定义的响应数据类型
     * @return ModuleConfig 模型
     */
    public static <REQ extends ProtocolHeader, RES extends ProtocolHeader> ServerCreator<REQ, RES> server(Class<REQ> requestType, Class<RES> responseType) {
        return new ServerCreator<>(Mojito.module(requestType, responseType));
    }

    public static <T extends ProtocolHeader, V extends ProtocolHeader> ClientCreator<T, V> client(Class<T> requestType, Class<V> responseType) {
        return new ClientCreator<>(Mojito.module(requestType, responseType));
    }

    public static class ServerCreator<REQ extends ProtocolHeader, RES extends ProtocolHeader> {
        private final Mojito<REQ, RES> config;

        public ServerCreator(Mojito<REQ, RES> config) {
            this.config = config;
        }

        public ServerCreator<REQ, RES> businessHandler(BusinessHandler<REQ, RES> serverHandler) {
            Protocol<REQ, RES> protocol = config.getCodecFactory().getProtocol();
            protocol.setBusinessHandler(serverHandler);
            return this;
        }

        public Server<?> create() {
            return config.getCodecFactory().getServer();
        }
    }


    public static class ClientCreator<REQ extends ProtocolHeader, RES extends ProtocolHeader> {

        private final Mojito<REQ, RES> config;

        private ClientCreator(Mojito<REQ, RES> serverConfig) {
            this.config = serverConfig;
        }

        public Client<REQ, RES> create() {
            return config.getCodecFactory().getClient();
        }

        public Client<REQ, RES> connect(String remoteHost, int remotePort) throws Exception {
            Client<REQ, RES> client = config.getCodecFactory().getClient(remoteHost, remotePort);
            client.connect(remoteHost, remotePort);
            return client;
        }

    }


}
