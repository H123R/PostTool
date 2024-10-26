package com.jax.PostTool.core.env;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class OrderedProperties extends Properties {
    private final Set<Object> propertyNameSet;
    public OrderedProperties() {
        propertyNameSet = new LinkedHashSet<>();
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        addKey(key);
        return super.put(key, value);
    }

    private void addKey(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("key类型错误, 必须为String");
        }
        propertyNameSet.add(key);
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        return new OrderedPropertiesEnumeration(propertyNameSet);
    }

    private static class OrderedPropertiesEnumeration implements Enumeration<Object> {

        Iterator<Object> iterator;

        private OrderedPropertiesEnumeration(Set<Object> propertyNameSet) {
            iterator = propertyNameSet.iterator();
        }

        @Override
        public boolean hasMoreElements() {
            return iterator.hasNext();
        }

        @Override
        public Object nextElement() {
            return iterator.next();
        }
    }

    @Override
    public Set<Object> keySet() {
        return propertyNameSet;
    }


    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        Set<Map.Entry<Object, Object>> original = super.entrySet();
        LinkedHashMap<Object, Map.Entry<Object, Object>> entryMap = new LinkedHashMap<>();
        for (Object propertyName : propertyNameSet) {
            entryMap.put(propertyName, null);
        }

        for (Map.Entry<Object, Object> entry : original) {
            entryMap.put(entry.getKey(), entry);
        }

        return new LinkedHashSet<>(entryMap.values());
    }

    @Override
    public synchronized void putAll(Map<?, ?> map) {
        for (Object key : map.keySet()) {
            addKey(key);
        }
        super.putAll(map);
    }

    @Override
    public synchronized void clear() {
        this.propertyNameSet.clear();
        super.clear();
    }

    @Override
    public synchronized Object remove(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("key类型错误, 必须为String");
        }
        return super.remove(key);
    }


    @Override
    public Set<String> stringPropertyNames() {
        Set<String> resSet = new LinkedHashSet<>();
        for (Object propertyName : propertyNameSet) {
            resSet.add((String) propertyName);
        }
        return resSet;
    }

    @Override
    public Enumeration<?> propertyNames() {
        return Collections.enumeration(propertyNameSet);
    }


}
