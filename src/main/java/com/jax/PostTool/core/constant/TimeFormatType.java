package com.jax.PostTool.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间类型
 *
 * @author jax
 * @date 2021/9/22 10:07
 */
@AllArgsConstructor
@Getter
public enum TimeFormatType {

    /**
     * 必要
     */
    TIME_SECOND(10, "秒戳", null),
    TIME_MILLISECOND(13, "毫秒戳", null),

    /**
     * 常用
     */
    DATE_WITH_DIVIDE(10, "年月日（带横线）", "yyyy-MM-dd"),
    DATE_WITH_DIVIDE_CHINESE(11, "年月日（带横线）(中文)", "yyyy年MM月dd日"),
    DATE_COMPLEX(19, "年月日时分秒(带格式)", "yyyy-MM-dd HH:mm:ss"),

    /**
     * 少用
     */
    DATE(8, "年月日", "yyyyMMdd"),
    DATE_MINUTE(12,"年月日时分", "yyyyMMddHHmm"),
    DATE_SECOND(14, "年月日时分秒", "yyyyMMddHHmmss"),
    DATE_COMPLEX_CHINESE(20, "年月日时分秒(带格式)(中文)", "yyyy年MM月dd日 HH:mm:ss"),
    DATE_COMPLEX_CHINESE_STANDARD(26, "年月日时分秒(带格式)(带时区)", "yyyy-MM-dd'T'HH:mm:ss+08:00"),

    ;

    private int len;

    private String name;

    private String format;

    public static TimeFormatType getInstance(int len) {
        for (TimeFormatType value : TimeFormatType.values()) {
            if (value.len == len) {
                return value;
            }
        }
        return TimeFormatType.DATE;
    }

    public static TimeFormatType getInstance(int len, String time) {
        for (TimeFormatType value : TimeFormatType.values()) {
            if (value.len == len) {
                return value;
            }
        }
        return TimeFormatType.DATE;
    }

}
