package cn.lxchinesszz.mojito.net.serialize;

import cn.lxchinesszz.mojito.net.serialize.impl.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuxin
 * 2022/8/5 23:24
 */
public enum SerializeEnum {

    PROTOSTUFF(1, ProtostuffObjectSerializer.class),

    COMPACT(2, NettyCompactObjectSerializer.class),
    HS2(3, Hession2ObjectSerializer.class),

    SIMPLE(4, NettyObjectSerializer.class),

    HS(5, HessionObjectSerializer.class),
    ;

    private byte type;

    private Class<? extends Serializer> serialize;

    SerializeEnum(int type, Class<? extends Serializer> serialize) {
        this.type = (byte) type;
        this.serialize = serialize;
    }

    public byte getType() {
        return type;
    }

    public Class<? extends Serializer> getSerialize() {
        return serialize;
    }

    private static final Map<Byte, SerializeEnum> cacheMap = new ConcurrentHashMap<>(values().length);

    static {
        for (SerializeEnum serializeEnum : values()) {
            cacheMap.put(serializeEnum.type, serializeEnum);
        }
    }

    public static SerializeEnum ofByType(Byte type) {
        return cacheMap.getOrDefault(type, PROTOSTUFF);
    }
}
