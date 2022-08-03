package cn.lxchinesszz.mojito.serialize.impl;

import cn.lxchinesszz.mojito.exception.DeserializeException;
import cn.lxchinesszz.mojito.exception.SerializeException;
import cn.lxchinesszz.mojito.serialize.AbstractSerializer;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;


/**
 * 注意:
 *
 * @author liuxin
 * 2020-07-31 21:32
 */
public class ProtostuffObjectSerializer extends AbstractSerializer {

    /**
     * 线程数会有限制,不会无穷大的使用
     */
    private static final ThreadLocal<LinkedBuffer> BUFFER = InheritableThreadLocal.withInitial(() ->
            LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

    @Override
    @SuppressWarnings("unchecked")
    public byte[] doSerialize(Object dataObject) throws SerializeException {
        // RuntimeSchema 懒加载内置缓存,所以我们不用在缓存了
        Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(dataObject.getClass());
        LinkedBuffer linkedBuffer = BUFFER.get();
        byte[] bytes = ProtostuffIOUtil.toByteArray(dataObject, schema, linkedBuffer);
        linkedBuffer.clear();
        return bytes;
    }

    @Override
    public Object doDeserialize(byte[] data) throws DeserializeException {
        throw new UnsupportedOperationException(getClass() + "必须指定反序列化类型");
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> dataType) throws DeserializeException {
        return schema(data, dataType);
    }

    public <T> T schema(byte[] data, Class<T> dataType) throws DeserializeException {
        try {
            Schema<T> schema = RuntimeSchema.getSchema(dataType);
            T dataTypeObj = dataType.newInstance();
            ProtostuffIOUtil.mergeFrom(data, dataTypeObj, schema);
            return dataTypeObj;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DeserializeException(e);
        }
    }
}
