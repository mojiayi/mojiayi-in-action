package com.mojiayi.action.javabasis.concurrent;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 并发地同步数据
 * </p>
 *
 * @author guangri.li
 * @since 2023/1/28 20:34
 */
public class SyncDataConcurrently {
    private AtomicInteger progressing = new AtomicInteger(0);

    private ConcurrentMap<String, Integer> taskThreadMap = new ConcurrentHashMap<>();

    private int total = 0;

    public void syncData(List<String> productIdList) {
        long startTime = System.currentTimeMillis();

        // 分配到不同线程执行数据同步
        int threadNum = getThreadNum();
        total = productIdList.size();
        ThreadPoolExecutor threadPoolExecutor = null;
        CountDownLatch countDownLatch = new CountDownLatch(total);
        try {
            threadPoolExecutor = new ThreadPoolExecutor(threadNum, threadNum, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(productIdList.size()));
            for (String productId : productIdList) {
                threadPoolExecutor.submit(() -> {
                    try {
                        executeDataSync(productId);
                        countDownLatch.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (threadPoolExecutor != null) {
                threadPoolExecutor.shutdown();
            }
        }
        int finishCount = taskThreadMap.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("同步" + finishCount + "个产品，用时" + (System.currentTimeMillis() - startTime) + "毫秒");
        for (Map.Entry<String, Integer> entry : taskThreadMap.entrySet()) {
            System.out.println("线程" + entry.getKey() + "处理数量=" + entry.getValue());
        }
    }

    private void executeDataSync(String productId) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        String threadName = Thread.currentThread().getName();
        int productCount = taskThreadMap.getOrDefault(threadName, 0);
        taskThreadMap.put(threadName, productCount + 1);
        Thread.sleep(new Random().nextInt(300));
        int finishCount = progressing.incrementAndGet();
        System.out.println("线程" + threadName + "，同步产品" + productId + "，用时" + (System.currentTimeMillis() - startTime) + "毫秒，进度=" + finishCount + "/" + total);
    }

    /**
     * 根据CPU核心数获取线程数量，为体现多线程特性，线程数量最小也是2
     * @return 线程数量
     */
    private int getThreadNum() {
        int cpu = Runtime.getRuntime().availableProcessors();

        return Math.max(cpu, 2);
    }
}
