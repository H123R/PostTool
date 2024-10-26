package com.jax.PostTool.core.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author jax
 * @date 2021/10/10 0:26
 */
public class StringUtils {

    public static String replaceFirst(String input, String target, String replacement) {
        int index = input.indexOf(target);
        if (index < 0) {
            return input;
        }
        String left = input.substring(0, index);
        String right = input.substring(index + target.length());
        StringBuilder builder = new StringBuilder();
        builder.append(left);
        builder.append(replacement);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(right)) {
            builder.append(right);
        }
        return builder.toString();
    }

    public static int countSubString(String input, String subStr) {
        int res = 0;
        int current = 0;

        while (-1 != (current = input.indexOf(subStr, current))) {
            current += subStr.length();
            res ++;
        }

        return res;

    }

    public static List<String> splitString(String input, String lineSplitReg) {
        String[] strList = input.split(lineSplitReg);
        return Arrays.asList(strList);
    }

}
