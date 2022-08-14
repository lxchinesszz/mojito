package cn.lxchinesszz.mojito.protocol;

import cn.lxchinesszz.mojito.serialize.SerializeEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 默认的协议头
 *
 * @author liuxin
 * 2022/8/5 23:16
 */
@ToString
public class ProtocolHeader implements Serializable {

    /**
     * 主协议
     *
     * @see ProtocolEnum
     */
    byte protocolType = ProtocolEnum.MOJITO.getType();

    /**
     * 序列化协议
     *
     * @see SerializeEnum
     */
    byte serializationType = SerializeEnum.HS.getType();

    Class<?> type;

    private String id;

    private Exception exception;

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    public ProtocolHeader() {
        this.id = String.valueOf(INVOKE_ID.incrementAndGet());
        this.type = getClass();
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public byte getSerializationType() {
        return serializationType;
    }


    public Class<?> getType() {
        return type;
    }

    private final Map<String, String> attachments = new ConcurrentHashMap<>();


    public String getAttachment(String key) {
        return (String) this.attachments.get(key);
    }

    public void setAttachment(String key, String value) {
        if (value == null) {
            this.attachments.remove(key);
        } else {
            this.attachments.put(key, value);
        }
    }

    public void removeAttachment(String key) {
        this.attachments.remove(key);
    }

    public Map<String, String> getAttachments() {
        return this.attachments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public void setSerializationType(byte serializationType) {
        this.serializationType = serializationType;
    }
}

