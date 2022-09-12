package cn.lxchinesszz.mojito.rpc.utils.reflect;

import cn.lxchinesszz.mojito.rpc.utils.PropertiesReflector;

import java.lang.reflect.Field;

/**
 * @author liuxin
 * 2022/8/28 23:32
 */
public class GetFieldInvoker implements JvmInvoker {
    private final Field field;

    public GetFieldInvoker(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
            if (PropertiesReflector.canControlMemberAccessible()) {
                field.setAccessible(true);
                return field.get(target);
            } else {
                throw e;
            }
        }
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
