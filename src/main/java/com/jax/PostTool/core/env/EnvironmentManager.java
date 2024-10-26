package com.jax.PostTool.core.env;

import com.alibaba.fastjson.JSONObject;
import com.jax.PostTool.core.constant.ClasspathConstants;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.*;

public class EnvironmentManager {

    private static final String DEFAULT_CONFIG_FILE = "/global.properties";
    private static final String OTHER_CONFIG_FILE = "/env-%s.properties";
    private static final String CURRENT_ENV = "current.env";

    private static Environment global;

    private static Environment current;

    private static final Map<String, Environment> allEnvironment = new HashMap<>();

    public static void start() {
        loadConfig();
    }

    public static void stop() {
        storeConfig();
    }

    public static <T> T getProperty(String name, Class<T> tClass) {
        String property = getProperty(name);
        if (StringUtils.isBlank(property)) {
            return null;
        }
        return JSONObject.parseObject(property, tClass);
    }

    public static String getProperty(String name) {
        if (current != null && current.containsProperty(name)) {
            return current.getProperty(name);
        }
        return global.getProperty(name);
    }

    public static void changeEnv(String env) {
        if (allEnvironment.containsKey(env)) {
            current = allEnvironment.get(env);
        } else {
            throw new RuntimeException("not exists env:" + env);
        }
    }

    public static String currentEnv() {
        return getProperty(CURRENT_ENV);
    }

    public static void putProperty(String name, String property) {
        if (current != null && current.containsProperty(name)) {
            current.putProperty(name, property);
            return;
        }
        global.putProperty(name, property);
    }

    private static void loadConfig() {
        Properties globalProperties = loadConfig(getGlobalFile());
        global = new Environment(globalProperties);

        Properties currentProperties = loadConfig(getCurrentFile());
        current = new Environment(currentProperties);
    }

    private static String getGlobalFile() {
        return ClasspathConstants.RESOURCES_PATH + DEFAULT_CONFIG_FILE;
    }

    private static String getCurrentFile() {
        String currentEnv = global.getProperty(CURRENT_ENV);
        String currentEnvFile = String.format(OTHER_CONFIG_FILE, currentEnv);
        return ClasspathConstants.RESOURCES_PATH + currentEnvFile;
    }

    private static void storeConfig() {
        Properties globalProperties = global.getProperties();
        storeConfig(globalProperties, getGlobalFile());

        Properties currentProperties = current.getProperties();
        storeConfig(currentProperties, getCurrentFile());
    }

    private static void storeConfig(Properties properties, String fileName) {
        Writer writer = getWriter(fileName);
        try {
            properties.store(writer, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SneakyThrows
    private static Writer getWriter(String fileName) {
        return new FileWriter(fileName);
    }

    @SneakyThrows
    private static Reader getReader(String fileName) {
        return new FileReader(fileName);
    }

    @SneakyThrows
    private static Properties loadConfig(String fileName) {
        Properties properties = new OrderedProperties();
        try (Reader reader = getReader(fileName)) {
            properties.load(reader);
        }
        return properties;
    }

}
