package com.jax.PostTool.core.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jax.PostTool.core.*;
import com.jax.PostTool.core.FormItem;
import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.Response;
import com.jax.PostTool.core.constant.ClasspathConstants;
import com.jax.PostTool.core.constant.HttpMethodEnum;
import com.jax.PostTool.core.constant.ResourcePathConstants;
import com.jax.PostTool.core.util.ThrowableUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashMap;


@Slf4j
public class LogInterceptor implements RequestInterceptor {

    @Override
    public int getOrder() {
        return 1000;
    }

    @Override
    public void preRequest(Request request) {
        logUrl(request);
        logHeaders(request);
        logParams(request);
        logBody(request);
        logForm(request);
    }

    @Override
    public void postResponse(Response response) {
        logRespHeaders(response);
        logResp(response);
        logDownloadResp(response);
    }

    private void logDownloadResp(Response response) {
        if (null == response.getDownloadLocation()) {
            return;
        }
        log.info("code: {}\r\nfile:\r\n{}", response.getResponseCode(), response.getDownloadLocation().getFileName());
    }

    private void logRespHeaders(Response response) {
        StringBuilder builder = new StringBuilder();
        response.getHeaders().getInstance().forEach((key, val) -> {
            builder.append(key);
            builder.append(": ");
            builder.append(val);
            builder.append("\r\n");
        });
        log.info("Response-Headers:\r\n{}", builder.toString());
    }

    @Override
    public void handleException(Request request, Response response, Exception ex) {
        if (response != null) {
            log.error("handleException, response:{}, e:{}", response.getRespStr(), ThrowableUtils.stackTraceToString(ex));
        }
    }

    private void logUrl(Request request) {
        log.info("======================================");
        log.info("caller:{}\r\nurl:\r\n{} {}", request.getCaller(), request.getHttpMethod(), request.getUrl());
    }

    private void logHeaders(Request request) {
        StringBuilder builder = new StringBuilder();
        request.getHeaders().getInstance().forEach((key, val) -> {
            builder.append(key);
            builder.append(": ");
            builder.append(val);
            builder.append("\r\n");
        });
        log.info("Headers:\r\n{}", builder.toString());
    }

    private void logParams(Request request) {
        if (request.getHttpMethod() == HttpMethodEnum.GET) {
            StringBuilder builder = new StringBuilder();
            request.getParams().getInstance().forEach((key, value) -> {
                builder.append(key);
                builder.append("=");
                builder.append(value);
                builder.append("\r\n");
            });
            String pretty = builder.toString();
            log.info("params:\r\n{}", pretty);
        }
    }

    private void logBody(Request request) {
        if (request.getHttpMethod() == HttpMethodEnum.POST) {
            if (request.getBody() == null) {
                return;
            }
            LinkedHashMap jsonObject = JSONObject.parseObject(request.getBody().getBodyStr(), LinkedHashMap.class);
            String pretty = JSONObject.toJSONString(jsonObject,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
            log.info("body:\r\n{}", pretty);
        }
    }

    private void logForm(Request request) {
        if (request.getHttpMethod() == HttpMethodEnum.POST) {
            if (null == request.getForm()) {
                return;
            }
            StringBuilder builder = new StringBuilder();
            for (String key : request.getForm().keySet()) {
                FormItem formItem = request.getForm().get(key);
                builder.append(key);
                builder.append("=");

                if (formItem.getFormType() == FormItem.FormType.FILE) {
                    String uploadFilePath = formItem.getValue();
                    String uploadFileLocalName = uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1);
                    builder.append(uploadFileLocalName);
                    builder.append("(File)");
                } else if (formItem.getFormType() == FormItem.FormType.STRING) {
                    builder.append(formItem.getValue());
                }
                builder.append("\r\n");
            }
            String pretty = builder.toString();
            log.info("form:\r\n{}", pretty);
        }
    }


    private void logResp(Response response) {
        if (StringUtils.isBlank(response.getRespStr())) {
            return;
        }
        String pretty = null;
        try {
            JSONObject respObj = JSONObject.parseObject(response.getRespStr());
            pretty = JSONObject.toJSONString(respObj,
                    SerializerFeature.PrettyFormat,
                    SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat);
        } catch (Exception e) {
            pretty = response.getRespStr();
        }
        log.info("code: {}\r\nresp:\r\n{}", response.getResponseCode(), pretty);
        try (Writer writer = new FileWriter(ResourcePathConstants.RESPONSE_JSON)) {
            writer.write(pretty);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
