package com.github.obiteaaron.commons.util;

import org.junit.Assert;
import org.junit.Test;

public class StringsTest {

    /**
     * 使用示例
     */
    @Test
    public void testMatch() {
        Assert.assertEquals(true, Strings.match("g*ks", "geeks")); // Yes
        Assert.assertEquals(true, Strings.match("ge?ks*", "geeksforgeeks")); // Yes
        Assert.assertEquals(false, Strings.match("g*k", "gee"));  // No because 'k' is not in second
        Assert.assertEquals(false, Strings.match("*pqrs", "pqrst")); // No because 't' is not in first
        Assert.assertEquals(true, Strings.match("abc*bcd", "abcdhghgbcd")); // Yes
        Assert.assertEquals(false, Strings.match("abc*c?d", "abcd")); // No because second must have 2 instances of 'c'
        Assert.assertEquals(true, Strings.match("*c*d", "abcd")); // Yes
        Assert.assertEquals(true, Strings.match("*?c*d", "abcd")); // Yes
        Assert.assertEquals(true, Strings.match("*?c***d", "abcd")); // Yes
        Assert.assertEquals(true, Strings.match("ge?ks**", "geeks")); // Yes
    }
}
