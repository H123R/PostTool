package com.jax.PostTool.custom;

import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.interceptor.AbstractPathFilterEnableInterceptor;

public class CommonHeaderInterceptor extends AbstractPathFilterEnableInterceptor {

    /**
     * 请求前拦截
     * @param request
     */
    public void preRequest(Request request) {
        request.getHeaders().header("Target-Service", "local");
    }

}
