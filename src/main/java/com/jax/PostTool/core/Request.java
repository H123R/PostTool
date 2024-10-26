package com.jax.PostTool.core;

import com.jax.PostTool.core.constant.HttpMethodEnum;
import lombok.Data;

import java.util.Map;

@Data
public class Request {
    // 调用者，为"最近一个"发起请求的类
    private String caller;
    private HttpMethodEnum httpMethod;
    private String url;
    private Headers headers;
    private Body body;
    private Form form;
    private Params params;
    private DownloadLocation downloadLocation;
    // 临时性的额外信息，用于request与response互通信息
    private Map<String, Object> extendsInfo;

}
