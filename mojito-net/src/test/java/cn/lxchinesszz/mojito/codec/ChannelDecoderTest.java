package cn.lxchinesszz.mojito.codec;

import cn.lxchinesszz.mojito.net.codec.MojitoChannelDecoder;
import cn.lxchinesszz.mojito.net.codec.MojitoChannelEncoder;
import cn.lxchinesszz.mojito.net.protocol.mojito.model.RpcRequest;
import cn.lxchinesszz.mojito.net.protocol.mojito.model.RpcResponse;
import cn.lxchinesszz.mojito.net.serialize.SerializeEnum;
import cn.lxchinesszz.mojito.net.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author liuxin
 * 2022/8/6 23:29
 */
class ChannelDecoderTest {

    @Test
    @DisplayName("使用换行符分隔符")
    void lineBasedFrameDecoder() {
        int maxLength = 100;
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes("hello world\nhello\nworld\n".getBytes(StandardCharsets.UTF_8));
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG),
                new LineBasedFrameDecoder(maxLength),
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf buf = ((ByteBuf) msg);
                        String content = buf.toString(StandardCharsets.UTF_8);
                        System.out.println(content);
                    }
                });
//        hello world
//        hello
//        world
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes(buffer));
    }

    @Test
    @DisplayName("自定义换行符分隔符")
    void delimiterBasedFrameDecoder() {
        int maxLength = 100;
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        buffer.writeBytes("hello world\nhello\rworld\\".getBytes(StandardCharsets.UTF_8));
        ByteBuf delimeter1 = Unpooled.buffer().writeBytes("\n".getBytes(StandardCharsets.UTF_8));
        ByteBuf delimeter2 = Unpooled.buffer().writeBytes("\r".getBytes(StandardCharsets.UTF_8));
        ByteBuf delimeter3 = Unpooled.buffer().writeBytes("\\".getBytes(StandardCharsets.UTF_8));
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG),
                new DelimiterBasedFrameDecoder(maxLength, delimeter1, delimeter2, delimeter3),
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf buf = ((ByteBuf) msg);
                        String content = buf.toString(StandardCharsets.UTF_8);
                        System.out.println(content);
                    }
                });
//        hello world
//        hello
//                world
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes(buffer));
    }

    @Test
    @DisplayName("固定长度进行拆解")
    void fixedLengthFrameDecoder() {
        //这里每条消息设置的固定长度是5
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG), new FixedLengthFrameDecoder(5),
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf buf = ((ByteBuf) msg);
                        String content = buf.toString(StandardCharsets.UTF_8);
                        System.out.println(content);
                    }
                });
//        hello
//        world
//        welco
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("helloworldwelcome".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    @DisplayName("动态获取长度报文")
    void lengthFieldBasedFrameDecoder() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        byte[] bytes = "hello world".getBytes(StandardCharsets.UTF_8);
        // 11
        System.out.println(bytes.length);
        // 4字节
        buffer.writeInt(bytes.length);
        // 真正的数据
        buffer.writeBytes(bytes);
        // 最大包长100字节
        int maxFrameLength = 100;
        // 从0开始,说明开头就是长度
        int lengthFieldOffset = 0;
        // 0 说明, 报文是有长度+真实数据组成的,没有其他的东西
        int lengthAdjustment = 0;
        // 跳过长度的字节，因为是int,所以是4字节
        int initialBytesToStrip = 4;
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG),
                new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, 4, lengthAdjustment, initialBytesToStrip),
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf buf = ((ByteBuf) msg);
                        String content = buf.toString(StandardCharsets.UTF_8);
                        System.out.println(content);
                    }
                });
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes(buffer));
    }


    private ByteBuf fillProtocol() throws Exception {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        RpcRequest rpcRequest = new RpcRequest();
        //1. 获取协议类型(1个字节)
        buffer.writeByte(rpcRequest.getProtocolType());
        //2. 获取序列化类型(1个字节)
        buffer.writeByte(rpcRequest.getSerializationType());
        //3. 根据序列化类型找到数据转换器生成二进制数据
        Serializer serializer = SerializeEnum.
                ofByType(rpcRequest.getSerializationType())
                .getSerialize().newInstance();
        byte[] data = serializer.serialize(rpcRequest);
        //4. 写入报文长度(4个字节)
        buffer.writeInt(data.length);
        //5. 写入报文内容(数组)
        buffer.writeBytes(data);


        //1. 获取协议类型(1个字节)
        buffer.writeByte(rpcRequest.getProtocolType());
        //2. 获取序列化类型(1个字节)
        buffer.writeByte(rpcRequest.getSerializationType());
        //3. 根据序列化类型找到数据转换器生成二进制数据
        //4. 写入报文长度(4个字节)
        buffer.writeInt(data.length);
        //5. 写入报文内容(数组)
        buffer.writeBytes(data);
        return buffer;
    }

    @Test
    @DisplayName("采用LengthFieldBasedFrameDecoder来解决拆包和黏包")
    public void testMojito2ChannelDecode() throws Exception {
        // 最大包长100字节
        int maxFrameLength = 10000;
        // 从0开始,说明开头就是长度
        int lengthFieldOffset = 2;
        // 0 说明, 报文是有长度+真实数据组成的,没有其他的东西
        int lengthAdjustment = 0;
        // 跳过长度的字节，因为是int,所以是4字节
        int initialBytesToStrip = 0;
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG),
                new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, 4, lengthAdjustment, initialBytesToStrip),
                new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        ByteBuf inByteBuf = (ByteBuf) msg;
                        // 协议类型
                        System.out.println("protocolType:" + inByteBuf.readByte());
                        // 序列化类型
                        byte serializationType = inByteBuf.readByte();
                        System.out.println("serializationType:" + serializationType);
                        // 数据长度
                        int dataSize = inByteBuf.readInt();
                        System.out.println("dataSize:" + dataSize);
                        byte[] dataArr = new byte[dataSize];
                        inByteBuf.readBytes(dataArr, 0, dataSize);

                        SerializeEnum serializeEnum = SerializeEnum.ofByType(serializationType);
                        Class<? extends Serializer> serialize = serializeEnum.getSerialize();
                        Serializer serializerNewInstance = (Serializer) serialize.newInstance();
                        //根据类型获取序列化器
                        Object deserialize = serializerNewInstance.deserialize(dataArr);
                        System.out.println(deserialize);
                    }
                });
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes(fillProtocol()));
    }

    @Test
    @DisplayName("SimpleChannelInboundHandler自动匹配支持的Java对象")
    public void test() throws Exception {
        // 根据自定义协议生成二进制数据流
        ByteBuf byteBuf = fillProtocol();
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG),
                new ChannelInitializer<EmbeddedChannel>() {
                    @Override
                    protected void initChannel(EmbeddedChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("a handler", new MojitoChannelDecoder());
                        pipeline.addLast("b handler",// 自定义一个String类型的
                                new SimpleChannelInboundHandler<RpcRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
                                        System.out.println("RpcRequest:" + msg);
                                        // 向下传播
                                        ctx.fireChannelRead(msg);
                                    }
                                });
                        pipeline.addLast("c handler", new SimpleChannelInboundHandler<Integer>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, Integer msg) throws Exception {
                                System.out.println("Integer:" + msg);
                            }
                        });
                    }
                });
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes(byteBuf));
    }


    @Test
    @DisplayName("出栈处理器")
    public void testOutbound() throws Exception {
        EmbeddedChannel channel = new EmbeddedChannel(new LoggingHandler(LogLevel.DEBUG),
                new ChannelInitializer<EmbeddedChannel>() {
                    @Override
                    protected void initChannel(EmbeddedChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("a handler", new MojitoChannelEncoder());
                        pipeline.addLast("b handler", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                System.out.println("Write:" + msg);
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                });
        RpcResponse rpcResponse = new RpcResponse();
        channel.writeOutbound(rpcResponse);
    }
}