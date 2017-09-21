package com.github.obiteaaron.commons.util;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Obite Aaron
 * @since 1.0
 */
public class DateDifferentTest {

    @Test
    public void main() {
        DateDifferent dateDifferent = DateDifferent.newInstance(DateDifferent.DAYS, DateDifferent.SECONDS);
        dateDifferent.terminal();
        dateDifferent.diff();
        System.out.println(dateDifferent.diff());
        System.out.println(DateDifferent.diff(DateTime.now().plusDays(-3).plusHours(-3).plusMinutes(-3), DateTime.now()));
        System.out.println(DateDifferent.diff(DateTime.now().plusDays(-3).plusHours(-3).plusMinutes(-3), DateTime.now(), DateDifferent.DAYS | DateDifferent.SECONDS));
        dateDifferent = DateDifferent.newInstance("耗时", DateDifferent.DAYS, DateDifferent.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dateDifferent.terminal();
        dateDifferent.diff();
        System.out.println(dateDifferent.diff());
    }
}
