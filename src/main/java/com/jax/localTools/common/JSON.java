package com.jax.localTools.common;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;

@Slf4j
public class JSON {

    @Test
    public void toPretty() {
        String input = "{\"name\": \"jax\"}";
        LinkedHashMap jsonObject = JSONObject.parseObject(input, LinkedHashMap.class);
        String pretty = JSONObject.toJSONString(jsonObject,
                SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue);
        log.info("pretty:\r\n{}", pretty);

    }

}
