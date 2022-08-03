package cn.lxchinesszz.mojito.test.print;

import lombok.SneakyThrows;

import java.math.BigDecimal;

/**
 * @author liuxin
 * 2022/6/5 01:09
 */
public class Sleeps {

    @SneakyThrows
    public static void sleep(double seconds) {
        long l = BigDecimal.valueOf(seconds).multiply(BigDecimal.valueOf(1000L)).longValue();
        Thread.sleep(l);
    }
}
