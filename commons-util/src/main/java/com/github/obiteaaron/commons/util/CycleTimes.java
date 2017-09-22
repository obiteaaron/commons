package com.github.obiteaaron.commons.util;

/**
 * @author Obite Aaron
 * @since 1.0
 */
public class CycleTimes {
    /**
     * 指定的代码执行指定的次数
     *
     * @param runnable 待运行代码
     * @param times    运行次数
     */
    public static void cycle(Runnable runnable, int times) {
        cycle(runnable, (long) times);
    }

    /**
     * 指定的代码执行指定的次数
     *
     * @param runnable 待运行代码
     * @param times    运行次数
     */
    public static void cycle(Runnable runnable, long times) {
        if (runnable == null || times <= 0)
            throw new IllegalArgumentException();
        for (long i = 0; i < times; i++) {
            runnable.run();
        }
    }
}
