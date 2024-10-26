package com.jax.localTools.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jax.PostTool.core.constant.TimeFormatType;
import com.jax.PostTool.core.util.ThrowableUtils;
import com.jax.localTools.dto.ConvertTimeResp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class Time {

    @Test
    public void printTime() {
        String time = "1729267200000";
        List<ConvertTimeResp> convertTimeRespList = printTime(time);
        String pretty = JSONObject.toJSONString(convertTimeRespList,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue);
        log.info(pretty);
    }


    private List<ConvertTimeResp> printTime(String time) {
        time = time.trim();
        TimeFormatType formatType = TimeFormatType.getInstance(time.length());
        Date date = null;
        if (StringUtils.isBlank(time)) {
            date = new Date();
        } else if (StringUtils.isBlank(formatType.getFormat())) {
            // 直接转秒戳或者毫秒戳
            if (TimeFormatType.TIME_SECOND == formatType) {
                date = new Date(1000L * Long.parseLong(time));
            } else if (TimeFormatType.TIME_MILLISECOND == formatType) {
                date = new Date(Long.parseLong(time));
            }
        } else {
            SimpleDateFormat format = new SimpleDateFormat(formatType.getFormat());
            try {
                date = format.parse(time);
            } catch (ParseException e) {
                log.warn("时间格式不支持: time:{}; e:{}", time, ThrowableUtils.stackTraceToString(e));
                throw new RuntimeException("时间格式不支持");
            }
        }
        return parseDate(date);
    }

    private List<ConvertTimeResp> parseDate(Date date) {
        List<ConvertTimeResp> timeRespList = new ArrayList<>();
        if (null == date) {
            return timeRespList;
        }
        for (TimeFormatType formatType : TimeFormatType.values()) {
            if (TimeFormatType.TIME_SECOND == formatType) {
                long second = date.getTime() / 1000L;
                ConvertTimeResp timeResp = new ConvertTimeResp(formatType.getName(), Integer.toString((int) second));
                timeRespList.add(timeResp);
            } else if (TimeFormatType.TIME_MILLISECOND == formatType) {
                long second = date.getTime();
                ConvertTimeResp timeResp = new ConvertTimeResp(formatType.getName(), Long.toString(second));
                timeRespList.add(timeResp);
            } else {
                SimpleDateFormat format = new SimpleDateFormat(formatType.getFormat());
                ConvertTimeResp timeResp = new ConvertTimeResp(formatType.getName(), format.format(date));
                timeRespList.add(timeResp);
            }
        }
        return timeRespList;
    }

}
