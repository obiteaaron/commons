package com.github.obiteaaron.commons.util;

/**
 * code come from {@code org.elasticsearch.common.Strings}
 * 一些字符串工具
 *
 * @author Obite Aaron
 * @since 1.0
 */
public final class Strings {
    /**
     * 转换成驼峰格式
     */
    public static String toCamelCase(String value) {
        return toCamelCase(value, (StringBuilder) null);
    }

    /**
     * 转换成驼峰格式
     */
    public static String toCamelCase(String value, StringBuilder sb) {
        boolean changed = false;

        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (c == 95 && i > 0) {
                if (!changed) {
                    if (sb != null) {
                        sb.setLength(0);
                    } else {
                        sb = new StringBuilder();
                    }

                    for (int j = 0; j < i; ++j) {
                        sb.append(value.charAt(j));
                    }

                    changed = true;
                }

                if (i < value.length() - 1) {
                    ++i;
                    sb.append(Character.toUpperCase(value.charAt(i)));
                }
            } else if (changed) {
                sb.append(c);
            }
        }

        if (!changed) {
            return value;
        } else {
            return sb.toString();
        }
    }

    /**
     * 转换成下划线格式
     */
    public static String toUnderscoreCase(String value) {
        return toUnderscoreCase(value, (StringBuilder) null);
    }

    /**
     * 转换成下划线格式
     */
    public static String toUnderscoreCase(String value, StringBuilder sb) {
        boolean changed = false;

        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (!Character.isUpperCase(c)) {
                if (changed) {
                    sb.append(c);
                }
            } else if (changed) {
                sb.append('_');
                sb.append(Character.toLowerCase(c));
            } else {
                if (sb != null) {
                    sb.setLength(0);
                } else {
                    sb = new StringBuilder();
                }

                for (int j = 0; j < i; ++j) {
                    sb.append(value.charAt(j));
                }

                changed = true;
                if (i == 0) {
                    sb.append(Character.toLowerCase(c));
                } else {
                    sb.append('_');
                    sb.append(Character.toLowerCase(c));
                }
            }
        }

        if (!changed) {
            return value;
        } else {
            return sb.toString();
        }
    }

    /**
     * 模式串pattern和字符串content是否匹配
     *
     * @param pattern 模式串,支持*号模糊多个匹配和?号单个匹配
     * @param content 字符串
     * @return true 匹配 | false 不匹配
     */
    public static boolean match(String pattern, String content) {
        return WildcardCharacter.test(pattern, content);
    }

    /**
     * 模式串pattern和字符串content是否匹配,支持*号模糊多个匹配和?号单个匹配
     */
    private static class WildcardCharacter {
        /**
         * @param pattern 匹配模式
         * @param content 待匹配内容
         * @param p       匹配模式的当前字符位置
         * @param c       内容的当前字符位置
         * @return true 匹配 | false 不匹配
         */
        static boolean match(String pattern, String content, int p, int c) {
            // if we reatch both end of two string, we are done
            if (pattern.length() == p && content.length() == c)
                return true;
            /** make sure that the characters after '*' are present in second string.
             this function assumes that the first string will not contain two
             consecutive '*'*/
            if (pattern.length() > p && '*' == pattern.charAt(p) && pattern.length() > p + 1 && content.length() == c)
                return match(pattern, content, p + 1, c);
            /** if the first string contains '?', or current characters of both strings match */
            if (pattern.length() > p && content.length() > c && ('?' == pattern.charAt(p) || pattern.charAt(p) == content.charAt(c)))
                return match(pattern, content, p + 1, c + 1);
            /** if there is *, then there are two possibilities
             a) We consider current character of second string
             b) We ignore current character of second string.*/
            if (pattern.length() > p && '*' == pattern.charAt(p))
                return match(pattern, content, p + 1, c) || match(pattern, content, p, c + 1);
            return false;
        }

        /**
         * @param pattern 匹配模式
         * @param content 待匹配内容
         * @return true 匹配 | false 不匹配
         */
        static boolean test(String pattern, String content) {
            if (null == pattern || null == content)
                return false;
            pattern = pattern.replaceAll("\\*{2,}", "*");
            return match(pattern, content, 0, 0);
        }
    }
}
