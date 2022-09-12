package cn.lxchinesszz.mojito.net.future.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author liuxin
 * 2020-08-25 21:24
 */
public class MojitoListeners<V> {

    private List<MojitoListener<V>> listeners;


    @SuppressWarnings("all")
    public MojitoListeners() {
        this.listeners = new ArrayList<>(4);
    }

    @SuppressWarnings("unused")
    public MojitoListeners(MojitoListener<V>[] listeners) {
        this.listeners = Arrays.asList(listeners);
    }

    @SuppressWarnings("all")
    public void add(MojitoListener<V> l) {
        listeners.add(l);
    }

    @SuppressWarnings("unused")
    public void remove(MojitoListener<V> l) {
        listeners.remove(l);
    }

    @SuppressWarnings("all")
    public MojitoListener<V>[] listeners() {
        return listeners.toArray(new MojitoListener[]{});
    }

    @SuppressWarnings("all")
    public int size() {
        return listeners.size();
    }
}
