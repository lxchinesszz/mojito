package cn.lxchinesszz.mojito.rpc.utils.reflect;

import cn.lxchinesszz.mojito.rpc.utils.PropertiesReflector;

import java.lang.reflect.Field;

/**
 * @author liuxin
 * 2022/8/28 23:33
 */
public class SetFieldInvoker implements JvmInvoker {

    private final Field field;

    public SetFieldInvoker(Field field) {
        this.field = field;
        this.field.setAccessible(true);
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException {
        try {
            field.set(target, args[0]);
        } catch (IllegalAccessException e) {
            if (PropertiesReflector.canControlMemberAccessible()) {
                field.setAccessible(true);
                field.set(target, args[0]);
            } else {
                throw e;
            }
        }
        return null;
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
