package cn.lxchinesszz.mojito.net.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liuxin
 * 2022/8/5 13:42
 */
public abstract class AbstractConstant<T extends AbstractConstant<T>> implements Constant<T> {

    private static final AtomicLong uniqueIdGenerator = new AtomicLong();
    private final int id;
    private final String name;

    private final long uniqueId;

    /**
     * Creates a new instance.
     */
    protected AbstractConstant(int id, String name) {
        // 同一种类型的常量id不会相同,但是不同类型的常量id可能相同
        this.id = id;
        this.name = name;
        // 所以需要uniqueId辅助保证常量不重复
        this.uniqueId = uniqueIdGenerator.getAndIncrement();
    }

    public long getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public int id() {
        return this.id;
    }


    @Override
    public String name() {
        return this.name;
    }

    @Override
    public int compareTo(T o) {
        if (this == o) {
            return 0;
        }
        @SuppressWarnings("UnnecessaryLocalVariable")
        AbstractConstant<T> other = o;
        int returnCode;
        returnCode = hashCode() - other.hashCode();
        if (returnCode != 0) {
            return returnCode;
        }

        if (uniqueId < other.getUniqueId()) {
            return -1;
        }
        if (uniqueId > other.getUniqueId()) {
            return 1;
        }

        throw new Error("failed to compare two different constants");
    }
}
