package com.jax.PostTool.core;

import com.jax.PostTool.core.constant.ClasspathConstants;
import com.jax.PostTool.core.constant.ResourcePathConstants;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

public class Form extends LinkedHashMap<String, FormItem> {

    public Map<String, FormItem> getInstance() {
        return this;
    }

    public static Form newForm() {
        return new Form();
    }

    public Form file(String fileName, @NotNull String filePath) {
        filePath = ResourcePathConstants.UPLOAD_FILE_DIR + filePath;
        put(fileName, new FormItem(FormItem.FormType.FILE, filePath));
        return this;
    }

    public Form form(String key, String val) {
        put(key, new FormItem(FormItem.FormType.STRING, val));
        return this;
    }

}
