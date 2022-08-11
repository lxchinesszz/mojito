package cn.lxchinesszz.mojito.codec;

import cn.lxchinesszz.mojito.serialize.SerializeEnum;
import cn.lxchinesszz.mojito.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author liuxin
 * 2020-07-31 19:34
 */
public class MojitoChannelDecoder extends ChannelDecoder {

    /**
     * 是否完整的消息头
     *
     * @param dataHeadSize 能读取到的数据
     * @return boolean
     */
    private boolean isFullMessageHeader(int dataHeadSize) {
        //因为一定会有数据所以只有大于5可能会完整,但是等于5一定不完整
        return dataHeadSize > 5;
    }

    /**
     * 是否完整消息
     *
     * @param buffer   数据buffer
     * @param dataSize
     * @return
     */
    private boolean isFullMessage(ByteBuf buffer, Integer dataSize) {
        //因为可能存在粘包的问题,所以大于等于就算本包完整了。
        return buffer.readableBytes() >= dataSize;
    }

    @Override
    protected void doDecode(ChannelHandlerContext ctx, ByteBuf inByteBuf, List<Object> out) throws Exception {
        //1. 不可读就关闭
        if (!inByteBuf.isReadable()) {
            Channel channel = ctx.channel();
            SocketAddress socketAddress = channel.remoteAddress();
            channel.close();
            System.err.println(">>>>>>>>>[" + socketAddress + "]客户端已主动断开连接....");
        } else {
            //2. 可读的数据大小
            int dataHeadSize = inByteBuf.readableBytes();
            //3. 不是完整的数据头就是6个字节 1协议 + 1序列化协议 + 4数据长度
            if (this.isFullMessageHeader(dataHeadSize)) {
                inByteBuf.markReaderIndex();
                // 协议类型
                byte protocolType = inByteBuf.readByte();
                // 序列化类型
                byte serializationType = inByteBuf.readByte();
                // 数据长度
                int dataSize = inByteBuf.readInt();
                // 如果不满足重置
                if (this.isFullMessage(inByteBuf, dataSize)) {
                    // 满足读取数据
                    byte[] dataArr = new byte[dataSize];
                    inByteBuf.readBytes(dataArr, 0, dataSize);
                    // 找到序列化器,性能有提升空间,可以序列化器可以进行池化
                    // Protostuff 必须根据class进行转换,所以这里也要优化
                    SerializeEnum serializeEnum = SerializeEnum.ofByType(serializationType);
                    Class<? extends Serializer> serialize = serializeEnum.getSerialize();
                    Serializer serializerNewInstance = (Serializer) serialize.newInstance();
                    //根据类型获取序列化器
                    Object deserialize = serializerNewInstance.deserialize(dataArr);
                    out.add(deserialize);
                } else {
                    inByteBuf.resetReaderIndex();
                    System.err.println("######################数据不足已重置buffer######################");
                }
            }
        }
    }
}
