package com.jax.PostTool.core;

import java.util.LinkedHashMap;
import java.util.Map;

public class Params {

    Map<String, String> instance = new LinkedHashMap<>();

    public Map<String, String> getInstance() {
        return instance;
    }

    public static Params newParams() {
        return new Params();
    }

    public Params param(String name, String value) {
        instance.put(name, value);
        return this;
    }

}
