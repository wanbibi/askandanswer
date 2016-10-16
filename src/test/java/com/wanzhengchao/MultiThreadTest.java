package com.wanzhengchao;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 16.10.17.
 */
class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(tid);
        }
    }
}


public class MultiThreadTest {
    public static void testThread() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        System.out.println(j);
                    }
                }
            }).start();
        }
    }

    private static Object obj = new Object();

    public static void testSynchronized1() {
        synchronized (obj) {
            for (int i = 0; i < 10; i++) {
                System.out.println("11111:  " + i);
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (obj) {
            for (int i = 0; i < 10; i++) {
                System.out.println("22222:  " + i);
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();
        }
    }

    public static void testExecutor() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("executor:  " + i);
                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    System.out.println("executor2:  " + i);
                }
            }
        });

        service.shutdown();
        while (!service.isTerminated()) {
            System.out.println("wait for termination");
        }
    }

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static void testAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 10; j++) {
                        try {
                            Thread.sleep(10);
                            System.out.println(atomicInteger.incrementAndGet());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
    public static void main(String[] args) {
        // testThread();
        testAtomic();
    }
}
