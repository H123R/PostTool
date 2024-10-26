package com.jax.PostTool.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class Headers {

    Map<String, String> instance = new LinkedHashMap<>();



    public Map<String, String> getInstance() {
        return instance;
    }

    public static Headers newHeader() {
        return new Headers();
    }

    public Headers header(String key, String value) {
        if (null != value) {
            this.instance.put(key, value);
        }
        return this;
    }

    public boolean contains(String key) {
        return this.instance.containsKey(key);
    }

}
