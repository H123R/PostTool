package com.jax.PostTool.core.interceptor;

import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.Response;
import org.springframework.core.Ordered;

/**
 * 请求拦截器，拦截的是请求前和响应后
 */
public interface RequestInterceptor extends Ordered {


    /**
     * 拦截器是否生效
     * @return
     */
    default boolean enable(Request request, Response response) {
        return true;
    }

    /**
     * 拦截器遵循先拦截请求后拦截响应的原则，即最先preRequest的拦截器最后postResponse
     * @return
     */
    @Override
    default int getOrder() {
        return 0;
    }

    /**
     * 请求前拦截
     * @param request
     */
    default void preRequest(Request request) {}

    /**
     * 响应后拦截
     * @param response
     */
    default void postResponse(Response response) {}

    /**
     * 请求后异常，即请求完成后可能存在发生异常的情况
     * @param request
     * @param response
     * @param ex
     */
    default void handleException(Request request, Response response, Exception ex) throws Exception {
        throw ex;
    }

}
