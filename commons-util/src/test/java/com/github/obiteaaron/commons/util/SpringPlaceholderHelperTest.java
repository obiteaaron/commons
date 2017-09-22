package com.github.obiteaaron.commons.util;

import org.junit.Test;

import java.util.Properties;

/**
 * @author Obite Aaron
 * @since 1.0
 */
public class SpringPlaceholderHelperTest {
    @Test
    public void test() {
        System.out.println(SpringPlaceholderHelper.resolve("今天想吃${meal}，但是没有${money}，吃不起${meal}，只能吃${food}", new Properties() {
            {
                setProperty("meal", "大餐");
                setProperty("money", "钱");
                setProperty("food", "便当");
            }
        }));
    }
}
