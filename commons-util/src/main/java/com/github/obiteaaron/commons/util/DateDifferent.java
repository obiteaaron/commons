package com.github.obiteaaron.commons.util;

import org.joda.time.*;

import java.util.ArrayList;

/**
 * @author Obite Aaron
 * @since 1.0
 */
public class DateDifferent {

    private static final String BLANK = " ";
    private static final String TIME_UNIT_YEARS = "年";
    private static final String TIME_UNIT_DAYS = "天";
    private static final String TIME_UNIT_HOURS = "小时";
    private static final String TIME_UNIT_MINUTES = "分钟";
    private static final String TIME_UNIT_SECONDS = "秒";
    private static final String TIME_UNIT_MILLISECONDS = "毫秒";

    private static final String DEFAULT_MESSAGE_PREFIX = "时间相差：";
    private String messagePrefix;
    //需要考虑 DateTime 类第一次加载的时间,大约60ms
    private DateTime startTime = DateTime.now();
    private DateTime endTime;

    /**
     * 掩码,用于确定展示哪些单位
     */
    public static final int YEARS = 0x00000001;
    public static final int DAYS = 0x00000010;
    public static final int HOURS = 0x00000100;
    public static final int MINUTES = 0x00001000;
    public static final int SECONDS = 0x00010000;
    public static final int MILLISECONDS = 0x00100000;

    /**
     * 所有掩码
     */
    private static final int[] allCode = new int[]{YEARS, DAYS, HOURS, MINUTES, SECONDS, MILLISECONDS};

    /**
     * 所有除法因子
     */
    private static final int[] allDivisor = new int[]{1, 365, 24, 60, 60, 1000};

    /**
     * 所有单位
     */
    private static final String[] allUnit = new String[]{TIME_UNIT_YEARS, TIME_UNIT_DAYS, TIME_UNIT_HOURS, TIME_UNIT_MINUTES, TIME_UNIT_SECONDS, TIME_UNIT_MILLISECONDS};

    /**
     * 掩码
     */
    private final int mask;

    /**
     * 实例中使用到的掩码
     */
    private int[] useCode;
    /**
     * 实例中使用到的掩码除法因子
     */
    private int[] useDivisor;
    /**
     * 实例中使用到的掩码单位
     */
    private String[] useUnit;

    /**
     * @param messagePrefix 前缀字符串
     * @param mask          输出掩码,最终掩码数字
     * @return DateDifferent
     */
    private DateDifferent(String messagePrefix, int mask) {
        this.messagePrefix = messagePrefix;
        this.mask = mask;
        init(this.mask);
    }

    /**
     * @return DateDifferent
     */
    public static DateDifferent newInstance() {
        return new DateDifferent(DEFAULT_MESSAGE_PREFIX, 0x11111111);
    }

    /**
     * @param messagePrefix 前缀字符串
     * @return DateDifferent
     */
    public static DateDifferent newInstance(String messagePrefix) {
        return new DateDifferent(messagePrefix, 0x11111111);
    }

    /**
     * @param codes 输出掩码数组
     * @return DateDifferent
     */
    public static DateDifferent newInstance(int... codes) {
        return newInstance(DEFAULT_MESSAGE_PREFIX, codes);
    }

    /**
     * @param messagePrefix 前缀字符串
     * @param codes         输出掩码数组
     * @return DateDifferent
     */
    public static DateDifferent newInstance(String messagePrefix, int... codes) {
        int localMask = 0;
        for (int i : codes) {
            localMask |= i;
        }
        return new DateDifferent(messagePrefix, localMask);
    }

    /**
     * 重置计时器，将开始时间设置为当前时间，重新开始计算
     *
     * @return 当前对象
     */
    public DateDifferent reset() {
        startTime = DateTime.now();
        endTime = null;
        return this;
    }

    /**
     * 结束计时，调用此方法后，可以调用{@link #diff()}获取最后的字符串，可以多次调用此方法，以获取多个计时
     *
     * @return 当前对象
     */
    public DateDifferent terminal() {
        endTime = DateTime.now();
        return this;
    }

    /**
     * @return 输出时间间隔字符串
     */
    public String diff() {
        StringBuilder diffBuilder = diffInner(startTime, endTime);
        if (diffBuilder.length() == messagePrefix.length()) {
            diffBuilder.append(BLANK).append(0).append(BLANK);
        }
        return diffBuilder.toString();
    }

    /**
     * 初始化掩码，如果是输出的相邻掩码之间还存在其它的掩码，如输出的是天、分钟，而实际应该是天、小时、分钟，那么分钟的除数不再是60，而是60 * 24.
     *
     * @param mask 掩码
     */
    private void init(int mask) {
        ArrayList<Integer> maskCode = new ArrayList<>(6);
        ArrayList<Integer> maskDivisor = new ArrayList<>(6);
        ArrayList<String> maskString = new ArrayList<>(6);
        int lastDivisor = 0;
        for (int i = 0; i < allCode.length; i++) {
            int code = allCode[i];
            if (isMask(mask, code)) {
                maskCode.add(allCode[i]);
                maskString.add(allUnit[i]);
                if (lastDivisor == 0) {
                    maskDivisor.add(allDivisor[i]);
                    lastDivisor = 1;
                } else {
                    maskDivisor.add(allDivisor[i] * lastDivisor);
                }
            } else {
                lastDivisor = allDivisor[i] * lastDivisor;
            }
        }
        useDivisor = new int[maskDivisor.size()];
        useUnit = new String[maskDivisor.size()];
        useCode = new int[maskCode.size()];
        for (int i = 0; i < useCode.length; i++) {
            useCode[i] = maskCode.get(i);
        }
        for (int i = 0; i < useDivisor.length; i++) {
            useDivisor[i] = maskDivisor.get(i);
        }
        for (int i = 0; i < useUnit.length; i++) {
            useUnit[i] = maskString.get(i);
        }
    }

    /**
     * 内部计算结果
     *
     * @param old 旧时间
     * @param now 新时间
     * @return 返回时间间隔字符串
     */
    private StringBuilder diffInner(DateTime old, DateTime now) {
        StringBuilder diffBuilder = new StringBuilder(messagePrefix);
        for (int i = 0; i < useCode.length; i++) {
            int code = useCode[i];
            int divisor = useDivisor[i];
            String unit = useUnit[i];
            append(diffBuilder, remainder(code, divisor, old, now), unit);
        }
        return diffBuilder;
    }

    /**
     * 计算剩余部分的数量。如几天、几小时、几分钟等等
     *
     * @param code    日期代码，{@link #YEARS,#DAYS,#HOURS,#MINUTES,#SECONDS,#MILLISECONDS}
     * @param divisor 除数，即对应code的除数，默认情况下是自然除数，如果有间隔，则会乘以间隔的除数。如，小时的除数是24，分钟的除数是60。如果小时的下一位输出是秒，则秒的除数是 60 * 60
     * @param old     旧时间
     * @param now     新时间
     * @return 除法结果的余数，即 几天、几小时等等
     */
    private long remainder(int code, int divisor, DateTime old, DateTime now) {
        switch (code) {
            case YEARS:
                return Years.yearsBetween(old, now).getYears() % divisor;
            case DAYS:
                return Days.daysBetween(old, now).getDays() % divisor;
            case HOURS:
                return Hours.hoursBetween(old, now).getHours() % divisor;
            case MINUTES:
                return Minutes.minutesBetween(old, now).getMinutes() % divisor;
            case SECONDS:
                return Seconds.secondsBetween(old, now).getSeconds() % divisor;
            case MILLISECONDS:
                return (now.getMillis() - old.getMillis()) % divisor;
            default:
                return 0;
        }
    }

    /**
     * 直接计算
     *
     * @param old 旧时间
     * @param now 新时间
     * @return 返回时间间隔字符串
     */
    public static String diff(DateTime old, DateTime now) {
        DateDifferent dateDifferent = DateDifferent.newInstance();
        dateDifferent.startTime = old;
        dateDifferent.endTime = now;
        return dateDifferent.diff();
    }

    /**
     * 直接计算
     *
     * @param old  旧时间
     * @param now  新时间
     * @param mask 掩码
     * @return 返回时间间隔字符串
     */
    public static String diff(DateTime old, DateTime now, int mask) {
        DateDifferent dateDifferent = DateDifferent.newInstance(mask);
        dateDifferent.startTime = old;
        dateDifferent.endTime = now;
        return dateDifferent.diff();
    }

    /**
     * 连接字符串
     *
     * @param diffBuilder StringBuilder
     * @param diff        时间间隔数字
     * @param unit        时间间隔单位
     */
    private void append(StringBuilder diffBuilder, long diff, String unit) {
        if (diff > 0) {
            diffBuilder.append(BLANK).append(diff).append(BLANK).append(unit);
        }
    }

    /**
     * 设置前缀字符串，默认值是 {@link #DEFAULT_MESSAGE_PREFIX}
     *
     * @param messagePrefix 前缀字符串
     */
    public void setMessagePrefix(String messagePrefix) {
        this.messagePrefix = messagePrefix;
    }

    /**
     * 用于判断某个掩码（code）是否被包含在整体输出掩码（mask）中
     *
     * @param mask 输出的整体掩码
     * @param code 单个掩码
     * @return true 包含 | false 不包含
     */
    public static boolean isMask(int mask, int code) {
        return (mask & code) == code;
    }
}