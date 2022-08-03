package cn.lxchinesszz.mojito.serialize.impl;

import cn.lxchinesszz.mojito.exception.DeserializeException;
import cn.lxchinesszz.mojito.exception.SerializeException;
import cn.lxchinesszz.mojito.serialize.AbstractSerializer;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author liuxin
 * 2020-07-31 21:32
 */
public class Hession2ObjectSerializer extends AbstractSerializer {

    @Override
    public byte[] doSerialize(Object dataObject) throws SerializeException {
        ByteArrayOutputStream dataArr = new ByteArrayOutputStream();
        Hessian2Output oeo = null;
        try {
            oeo = new Hessian2Output(dataArr);
            oeo.writeObject(dataObject);
            oeo.flush();
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
        return dataArr.toByteArray();
    }

    @Override
    public Object doDeserialize(byte[] data) throws DeserializeException {
        Object o;
        Hessian2Input odi = new Hessian2Input(new ByteArrayInputStream(data));
        try {
            o = odi.readObject();
        } catch (IOException e) {
            throw new DeserializeException(e);
        }
        return o;
    }

}
