package cn.lxchinesszz.mojito.serialize.impl;

import cn.lxchinesszz.mojito.exception.DeserializeException;
import cn.lxchinesszz.mojito.exception.SerializeException;
import cn.lxchinesszz.mojito.serialize.AbstractSerializer;
import cn.lxchinesszz.mojito.serialize.Serializer;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author liuxin
 * 2020-07-31 21:32
 */
public class HessionObjectSerializer extends AbstractSerializer {

    @Override
    public byte[] doSerialize(Object dataObject) throws SerializeException {
        HessianOutput oeo = null;
        try (ByteArrayOutputStream dataArr = new ByteArrayOutputStream()) {
            oeo = new HessianOutput(dataArr);
            oeo.writeObject(dataObject);
            oeo.flush();
            return dataArr.toByteArray();
        } catch (IOException e) {
            throw new SerializeException(e);
        } finally {
            if (oeo != null) {
                try {
                    oeo.close();
                } catch (IOException e) {
                    throw new SerializeException(e);
                }
            }
        }
    }

    @Override
    public Object doDeserialize(byte[] data) throws DeserializeException {
        Object o;
        HessianInput odi = new HessianInput(new ByteArrayInputStream(data));
        try {
            o = odi.readObject();
        } catch (IOException e) {
            throw new DeserializeException(e);
        }
        return o;
    }

}
