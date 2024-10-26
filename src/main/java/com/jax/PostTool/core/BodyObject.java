package com.jax.PostTool.core;

import com.jax.PostTool.core.constant.TimeFormatType;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;


@Slf4j
public class BodyObject extends LinkedHashMap<String, Object>  {



    public BodyObject addObj(String name, BodyObject obj) {
        put(name, obj);
        return this;
    }

    public BodyObject addList(String name, BodyList list) {
        put(name, list);
        return this;
    }

    public BodyObject addString(String name, String string) {
        put(name, string);
        return this;
    }

    public BodyObject addInt(String name, Integer integer) {
        put(name, integer);
        return this;
    }

    public BodyObject addBoolean(String name, Boolean bool) {
        put(name, bool);
        return this;
    }

    public BodyObject addDate(String name, String date) {
        try {
            Date parse = null;
            if (date.contains("-")) {
                if (date.length() == TimeFormatType.DATE_WITH_DIVIDE.getFormat().length()) {
                    SimpleDateFormat format = new SimpleDateFormat(TimeFormatType.DATE_WITH_DIVIDE.getFormat());
                    parse = format.parse(date);
                } else if (date.length() == TimeFormatType.DATE_COMPLEX.getFormat().length()) {
                    SimpleDateFormat format = new SimpleDateFormat(TimeFormatType.DATE_COMPLEX.getFormat());
                    parse = format.parse(date);
                }
            } else {
                if (date.length() == TimeFormatType.TIME_SECOND.getLen()) {
                    parse = new Date(1000L * Long.parseLong(date));
                } else if (date.length() == TimeFormatType.TIME_MILLISECOND.getLen()) {
                    parse = new Date(Long.parseLong(date));
                }
            }
            SimpleDateFormat format = new SimpleDateFormat(TimeFormatType.DATE_COMPLEX_CHINESE_STANDARD.getFormat());
            put(name, format.format(parse));
            return this;
        } catch (Exception e) {
            log.error("addDate error", e);
            return this;
        }

    }

}
