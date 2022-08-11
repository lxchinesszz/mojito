package cn.lxchinesszz.mojito.future;

import cn.lxchinesszz.mojito.future.listener.MojitoListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author liuxin
 * 2022/8/9 22:19
 */
public class MojitoFutureTest {


    @Test
    @DisplayName("模拟12个线程并发执行set成功,只会通知一次,打印第一个值")
    public void test() throws Exception {
        MojitoFuture<Integer> future = new MojitoFuture<>();
        future.addListeners(new MojitoListener<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                System.out.println("收到结果:" + result);
            }

            @Override
            public void onThrowable(Throwable throwable) {

            }
        });
        new Thread(() -> {
            try {
                Integer integer = future.get();
                System.out.println("输出" + integer);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).start();

        CountDownLatch countDownLatch = new CountDownLatch(12);
        for (int i = 0; i < 12; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    countDownLatch.await();
                    System.out.println("成功" + System.currentTimeMillis());
                    future.setSuccess(finalI);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }).start();
            countDownLatch.countDown();
        }

    }

    @Test
    @DisplayName("模拟超时场景")
    public void testTimeout() throws Exception {
        Assertions.assertThrowsExactly(TimeoutException.class, () -> {
            MojitoFuture<Integer> future = new MojitoFuture<>();
            future.addListeners(new MojitoListener<Integer>() {
                @Override
                public void onSuccess(Integer result) {
                    System.out.println("收到结果:" + result);
                }

                @Override
                public void onThrowable(Throwable throwable) {

                }
            });
            future.get(3, TimeUnit.SECONDS);
        });
    }
}