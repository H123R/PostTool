package com.jax.PostTool.core.interceptor;

import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.env.EnvironmentManager;

public class HostPreInterceptor implements RequestInterceptor {

    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public void preRequest(Request request) {
        String host = EnvironmentManager.getProperty("host");
        request.setUrl(host + request.getUrl());
    }

}
