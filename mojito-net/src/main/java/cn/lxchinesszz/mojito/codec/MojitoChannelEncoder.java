package cn.lxchinesszz.mojito.codec;

import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.serialize.SerializeEnum;
import cn.lxchinesszz.mojito.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * 对服务端响应客户端的编码
 *
 * @author liuxin
 * 2020-07-31 19:34
 */
public class MojitoChannelEncoder extends ChannelEncoder<ProtocolHeader> {

    @Override
    protected void doEncode(ChannelHandlerContext ctx, ProtocolHeader msg, ByteBuf out) throws Exception {
        //1. 获取协议类型(1个字节)
        out.writeByte(msg.getProtocolType());
        //2. 获取序列化类型(1个字节)
        out.writeByte(msg.getSerializationType());
        //3. 根据序列化类型找到数据转换器生成二进制数据
        //FIXME 每次都实例化有点浪费时间,可以进行池化
        Serializer serializer = SerializeEnum.
                ofByType(msg.getSerializationType())
                .getSerialize().newInstance();
        byte[] data = serializer.serialize(msg);
        //4. 写入报文长度(4个字节)
        out.writeInt(data.length);
        //5. 写入报文内容(数组)
        out.writeBytes(data);
    }
}
