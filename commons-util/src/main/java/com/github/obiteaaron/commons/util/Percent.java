package com.github.obiteaaron.commons.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 计算百分比，并可按设定的百分比间隔回调
 *
 * @author Obite Aaron
 * @since 1.0
 */
public class Percent {

    private AtomicLong done;
    private AtomicLong count;

    /**
     * 最后一次回调的百分比
     */
    private int lastCallback = 0;
    /**
     * 每一个需要回调的百分比间隔，默认 1%
     */
    private int perSlice = 1;
    /**
     * 回调函数
     */
    private Callback callback;

    /**
     * 创建一个新实例
     *
     * @param count 总数
     * @return
     */
    public static Percent newPercent(long count) {
        return new Percent(count);
    }

    private Percent(long count) {
        this.count = new AtomicLong(count);
        this.done = new AtomicLong();
    }

    /**
     * @param done 完成了几个，用于计算完成进度。如果完成总数超过count，计算结果可能大于100%
     * @return
     */
    public Percent done(long done) {
        this.done.addAndGet(done);

        if (callback != null) {
            int hasComplete = hasComplete();
            if (hasComplete - lastCallback >= perSlice) {
                lastCallback = hasComplete;
                callback.call(hasComplete);
            }
        }

        return this;
    }

    /**
     * @param perSlice 1~100
     * @param callback 回调函数
     * @return
     */
    public Percent setCallback(int perSlice, Callback callback) {
        this.callback = callback;
        this.perSlice = perSlice > 0 ? perSlice : 1;
        return this;
    }

    /**
     * @return 1~100
     */
    public int hasComplete() {
        return (int) (done.get() * 1.0D / count.get() * 100);
    }

    public long getDone() {
        return done.get();
    }

    public void setDone(long done) {
        this.done.set(done);
    }

    public long getCount() {
        return count.get();
    }

    public void setCount(long count) {
        this.count.set(count);
    }

    /**
     * 用于完成一定的进度后回调
     */
    public interface Callback {
        /**
         * @param hasComplete 完成百分比 1~100
         */
        void call(int hasComplete);
    }

}
