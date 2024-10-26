package com.jax.PostTool.core.env;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Environment {

    public Environment(Properties properties) {
        this.properties = properties;
    }

    @Getter
    private final Properties properties;

    public String getProperty(String name) {
        return properties.getProperty(name);
    }

    public void putProperty(String name, String property) {
        properties.put(name, property);
    }

    public boolean containsProperty(String name) {
        return properties.containsKey(name);
    }

}
