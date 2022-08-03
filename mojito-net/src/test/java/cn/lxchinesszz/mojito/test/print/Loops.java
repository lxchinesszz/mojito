package cn.lxchinesszz.mojito.test.print;

import java.util.function.Consumer;

/**
 * @author liuxin
 * 2022/5/18 16:03
 */
public class Loops {

    public static void loop(Consumer<Integer> consumer) {
        loop(3, consumer);
    }

    public static void loop(Integer loopCount, Consumer<Integer> consumer) {
        for (int i = 0; i < loopCount; i++) {
            consumer.accept(i);
        }
    }
}
