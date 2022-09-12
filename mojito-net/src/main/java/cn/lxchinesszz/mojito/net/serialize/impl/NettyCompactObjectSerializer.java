package cn.lxchinesszz.mojito.net.serialize.impl;

import cn.lxchinesszz.mojito.net.exception.DeserializeException;
import cn.lxchinesszz.mojito.net.exception.SerializeException;
import cn.lxchinesszz.mojito.net.serialize.AbstractSerializer;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ClassResolvers;

import java.io.*;

/**
 * @author liuxin
 * 2020-07-31 21:10
 */
public class NettyCompactObjectSerializer extends AbstractSerializer {

    @Override
    public byte[] doSerialize(Object dataObject) throws SerializeException {
        try (ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
             CompactObjectOutputStream oeo = new CompactObjectOutputStream(dataArr)) {
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
        try (CompactObjectInputStream odi = new CompactObjectInputStream(new ByteArrayInputStream(data), ClassResolvers.cacheDisabled(null))) {
            o = odi.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new DeserializeException(e);
        }
        return o;
    }

    private static class CompactObjectOutputStream extends ObjectOutputStream {

        static final int TYPE_FAT_DESCRIPTOR = 0;
        static final int TYPE_THIN_DESCRIPTOR = 1;

        CompactObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            writeByte(STREAM_VERSION);
        }

        @Override
        protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
            Class<?> clazz = desc.forClass();
            if (clazz.isPrimitive() || clazz.isArray() || clazz.isInterface() ||
                    desc.getSerialVersionUID() == 0) {
                write(TYPE_FAT_DESCRIPTOR);
                super.writeClassDescriptor(desc);
            } else {
                write(TYPE_THIN_DESCRIPTOR);
                writeUTF(desc.getName());
            }
        }
    }

    /**
     * 压缩
     */
    private static class CompactObjectInputStream extends ObjectInputStream {

        static final int TYPE_FAT_DESCRIPTOR = 0;

        static final int TYPE_THIN_DESCRIPTOR = 1;

        private final ClassResolver classResolver;

        CompactObjectInputStream(InputStream in, ClassResolver classResolver) throws IOException {
            super(in);
            this.classResolver = classResolver;
        }

        @Override
        protected void readStreamHeader() throws IOException {
            int version = readByte() & 0xFF;
            if (version != STREAM_VERSION) {
                throw new StreamCorruptedException(
                        "Unsupported version: " + version);
            }
        }

        @Override
        protected ObjectStreamClass readClassDescriptor()
                throws IOException, ClassNotFoundException {
            int type = read();
            if (type < 0) {
                throw new EOFException();
            }
            switch (type) {
                case TYPE_FAT_DESCRIPTOR:
                    return super.readClassDescriptor();
                case TYPE_THIN_DESCRIPTOR:
                    String className = readUTF();
                    Class<?> clazz = classResolver.resolve(className);
                    return ObjectStreamClass.lookupAny(clazz);
                default:
                    throw new StreamCorruptedException(
                            "Unexpected class descriptor type: " + type);
            }
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            Class<?> clazz;
            try {
                clazz = classResolver.resolve(desc.getName());
            } catch (ClassNotFoundException ignored) {
                clazz = super.resolveClass(desc);
            }

            return clazz;
        }
    }
}
