package com.jax.PostTool.core.util;

import com.jax.PostTool.core.Headers;
import lombok.Data;

@Data
public class ConnectResult {

    // 开启连接耗时(单位：纳秒)
    long durationOpenConnect;
    // 发送请求至服务端耗时(单位：纳秒)
    long durationSendRequest;
    // 服务端响应耗时(单位：纳秒)
    long durationReceiveResponse;
    // 下载响应耗时(单位：纳秒)
    long durationDownloadResponse;
    // 响应码
    int responseCode;
    // 响应字符串
    String responseStr;
    // 响应头
    private Headers headers;



}
