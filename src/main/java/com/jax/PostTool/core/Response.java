package com.jax.PostTool.core;

import lombok.Data;

import java.util.Map;
@Data
public class Response {

    public Response(RequestTemplate executeTemplate) {
        this.executeTemplate = executeTemplate;
    }

    private RequestTemplate executeTemplate;

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
    private String respStr;
    // 响应头
    private Headers headers;
    // 文件下载的位置
    private DownloadLocation downloadLocation;
    // 临时性的额外信息，用于request与response互通信息
    private Map<String, Object> extendsInfo;

}
