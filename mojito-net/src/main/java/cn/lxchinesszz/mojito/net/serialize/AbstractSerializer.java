package cn.lxchinesszz.mojito.net.serialize;

import cn.lxchinesszz.mojito.net.exception.DeserializeException;
import cn.lxchinesszz.mojito.net.exception.SerializeException;

import java.io.Serializable;
import java.text.MessageFormat;


/**
 * @author liuxin
 * 2022/7/30 14:28
 */
public abstract class AbstractSerializer implements Serializer {

    @Override
    public byte[] serialize(Object dataObject) throws SerializeException {
        if (!(dataObject instanceof Serializable)) {
            throw new SerializeException(MessageFormat.format("{0},未实现java.io.Serializable接口", dataObject));
        }
        return doSerialize(dataObject);
    }

    @Override
    public Object deserialize(byte[] data) throws DeserializeException {
        return doDeserialize(data);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(byte[] data, Class<T> dataType) throws DeserializeException {
        return (T) doDeserialize(data);
    }

    public abstract byte[] doSerialize(Object dataObject) throws SerializeException;

    public abstract Object doDeserialize(byte[] data) throws DeserializeException;

}
