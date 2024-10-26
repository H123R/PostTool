package com.jax.PostTool.core.interceptor;

import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.Response;

/**
 * 调用者(调用类)包含当前RequestInterceptor的包路径时，RequestInterceptor才生效
 */
public abstract class AbstractPathFilterEnableInterceptor implements RequestInterceptor {

    @Override
    public boolean enable(Request request, Response response) {
        return request.getCaller().contains(this.getClass().getPackage().getName() + ".");
    }

}
