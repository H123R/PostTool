package com.jax.PostTool.core;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.jax.PostTool.core.constant.TimeFormatType;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Body extends LinkedHashMap<String, Object> {

    @JSONField(serialize=false)
    private boolean serialized = false;

    @JSONField(serialize=false)
    private String bodyStr;

    public Map<String, Object> getInstance() {
        return this;
    }

    public String getBodyStr() {
        if (!serialized) {
            bodyStr = JSONObject.toJSONString(getInstance());
            serialized = true;
        }
        return bodyStr;
    }

    public static Body newBody() {
        return new Body();
    }

    public static Body newBody(String bodyStr) {
        Body cur = new Body();
        cur.bodyStr = bodyStr;
        cur.serialized = true;
        return cur;
    }

    public Body addObj(String name, BodyObject obj) {
        put(name, obj);
        serialized = false;
        return this;
    }

    public Body addList(String name, BodyList list) {
        put(name, list);
        serialized = false;
        return this;
    }

    public Body addString(String name, String string) {
        put(name, string);
        serialized = false;
        return this;
    }

    public Body addInt(String name, Integer integer) {
        put(name, integer);
        serialized = false;
        return this;
    }

    public Body body(String name, Object obj) {
        put(name, obj);
        serialized = false;
        return this;
    }

    public Body addDate(String name, String date) {
        serialized = false;
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

    public void resetBody(String bodyStr) {
        this.bodyStr = bodyStr;
    }

}
