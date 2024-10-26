package com.jax.PostTool.core.interceptor;

import com.jax.PostTool.core.Response;
import com.jax.PostTool.core.util.SimpleHttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * tips: 请关注计时实现 {@link SimpleHttpUtils#connect}
 */
@Slf4j
public class CalDurationInterceptor implements RequestInterceptor {


    @Override
    public int getOrder() {
        return 10000;
    }

    @Override
    public void postResponse(Response response) {
        log.info("主耗时: {}ms\r\n" +
                "开启连接耗时: {}ms\r\n" +
                "发送请求至服务端耗时: {}ms\r\n" +
                "服务端响应耗时: {}ms\r\n" +
                "下载响应耗时: {}ms\r\n",
                toMilliSecond(response.getDurationReceiveResponse() + response.getDurationDownloadResponse()),
                toMilliSecond(response.getDurationOpenConnect()),
                toMilliSecond(response.getDurationSendRequest()),
                toMilliSecond(response.getDurationReceiveResponse()),
                toMilliSecond(response.getDurationDownloadResponse()));
    }

    private String toMilliSecond(long duration) {
        return new BigDecimal(duration).divide(new BigDecimal(1000000), 2, RoundingMode.HALF_UP).toString();
    }

}
