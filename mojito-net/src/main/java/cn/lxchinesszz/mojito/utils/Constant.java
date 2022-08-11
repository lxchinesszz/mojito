package cn.lxchinesszz.mojito.utils;

/**
 * @author liuxin
 * 2022/8/5 10:06
 */
public interface Constant<T extends Constant<T>> extends Comparable<T> {

    int id();

    String name();
}
