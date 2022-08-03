package cn.lxchinesszz.mojito.serialize.impl;

import cn.lxchinesszz.mojito.exception.DeserializeException;
import cn.lxchinesszz.mojito.exception.SerializeException;
import cn.lxchinesszz.mojito.serialize.AbstractSerializer;
import io.netty.handler.codec.serialization.ObjectDecoderInputStream;
import io.netty.handler.codec.serialization.ObjectEncoderOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 必须要实现Serializable接口,增加一个长度验证
 *
 * @author liuxin
 * 2020-07-31 20:16
 */
public class NettyObjectSerializer extends AbstractSerializer {


    @Override
    public byte[] doSerialize(Object dataObject) throws SerializeException {
        try (ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
             ObjectEncoderOutputStream oeo = new ObjectEncoderOutputStream(dataArr)) {
            oeo.writeObject(dataObject);
            oeo.flush();
            return dataArr.toByteArray();
        } catch (IOException e) {
            throw new SerializeException(e);
        }
    }

    @Override
    public Object doDeserialize(byte[] data) throws DeserializeException {
        Object o;
        try (ObjectDecoderInputStream odi = new ObjectDecoderInputStream(new ByteArrayInputStream(data))) {
            o = odi.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new DeserializeException(e);
        }
        return o;
    }


}
