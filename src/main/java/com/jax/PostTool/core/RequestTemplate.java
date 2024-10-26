package com.jax.PostTool.core;

import com.jax.PostTool.core.constant.HttpMethodEnum;
import com.jax.PostTool.core.constant.ResourcePathConstants;
import com.jax.PostTool.core.env.EnvironmentManager;
import com.jax.PostTool.core.interceptor.RequestInterceptor;
import com.jax.PostTool.core.interceptor.RequestInterceptorRegister;
import com.jax.PostTool.core.util.ConnectResult;
import com.jax.PostTool.core.util.SimpleHttpUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

@Slf4j
public class RequestTemplate {

    private RequestBuilder requestBuilder;

    private List<RequestInterceptor> requestInterceptorList;

    private Stack<RequestInterceptor> reverseRequestInterceptorStack = new Stack<>();


    public Response request() {
        start();
        try {
            return doRequest();
        } finally {
            stop();
        }
    }

    public Response requestAndDownload(@NotBlank String fileName) {
        requestBuilder.downloadLocation(new DownloadLocation(fileName));
        return request();
    }

    public static void start() {
        EnvironmentManager.start();
    }


    public static void stop() {
        EnvironmentManager.stop();
    }

    public Response retryRequest() {
        return doRequest();
    }

    public Response doRequest() {
        setCaller();
        Request request = requestBuilder.build();
        Response response = new Response(this);
        response.setExtendsInfo(request.getExtendsInfo());
        try {
            preRequest(request, response);
            if (request.getHttpMethod() == HttpMethodEnum.GET) {
                get(request, response);
            } else if (request.getHttpMethod() == HttpMethodEnum.POST) {
                if (request.getForm() != null) {
                    postByForm(request, response);
                } else {
                    postByJson(request, response);
                }
            } else {
                throw new RuntimeException("not supply this method:" + request.getHttpMethod().name());
            }
            postResponse(request, response);
        } catch (Exception e) {
            handleException(request, response, e);
        }
        return response;
    }

    private void setCaller() {
        try {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            boolean breakFlag = false;
            for (StackTraceElement element : stackTrace) {
                Class<?> aClass = this.getClass().getClassLoader().loadClass(element.getClassName());
                for (Method declaredMethod : aClass.getDeclaredMethods()) {
                    if (Objects.equals(declaredMethod.getName(), element.getMethodName())) {
                        if (declaredMethod.isAnnotationPresent(Test.class)) {
                            requestBuilder.caller(element.getClassName());
                            breakFlag = true;
                            break;
                        }
                    }
                }
                if (breakFlag) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("setCaller fail");
            throw new RuntimeException(e);
        }

    }

    private void postResponse(Request request, Response response) {
        while (!reverseRequestInterceptorStack.empty()) {
            RequestInterceptor requestInterceptor = reverseRequestInterceptorStack.pop();
            if (requestInterceptor.enable(request, response)) {
                requestInterceptor.postResponse(response);
            }
        }
    }

    private void preRequest(Request request, Response response) {
        for (RequestInterceptor requestInterceptor : requestInterceptorList) {
            if (requestInterceptor.enable(request, response)) {
                reverseRequestInterceptorStack.push(requestInterceptor);
                requestInterceptor.preRequest(request);
            }
        }
    }

    @SneakyThrows
    private void handleException(Request request, Response response, Exception e) {
        while (!reverseRequestInterceptorStack.empty()) {
            RequestInterceptor requestInterceptor = reverseRequestInterceptorStack.pop();
            if (requestInterceptor.enable(request, response)) {
                requestInterceptor.handleException(request, response, e);
            }
        }
    }

    private void postByForm(Request request, Response response) {
        ConnectResult connectResult = SimpleHttpUtils.postByForm(request.getUrl(),
                request.getHeaders().getInstance(),
                request.getForm().getInstance(),
                packDownloadPath(request)
        );
        fillResponse(request, connectResult, response);
    }

    private void postByJson(Request request, Response response) {
        ConnectResult connectResult = SimpleHttpUtils.postByJson(request.getUrl(),
                request.getHeaders().getInstance(),
                request.getBody().getBodyStr(),
                packDownloadPath(request)
                );
        fillResponse(request, connectResult, response);
    }

    private String packDownloadPath(Request request) {
        DownloadLocation downloadLocation = request.getDownloadLocation();
        String downloadPath = null;
        if (null != downloadLocation) {
            downloadPath = ResourcePathConstants.DOWNLOAD_FILE_DIR + downloadLocation.getFileName();
        }
        return downloadPath;
    }

    private void fillResponse(Request request, ConnectResult connectResult, Response response) {
        response.setResponseCode(connectResult.getResponseCode());
        response.setDurationOpenConnect(connectResult.getDurationOpenConnect());
        response.setDurationSendRequest(connectResult.getDurationSendRequest());
        response.setDurationReceiveResponse(connectResult.getDurationReceiveResponse());
        response.setDurationDownloadResponse(connectResult.getDurationDownloadResponse());
        response.setRespStr(connectResult.getResponseStr());
        response.setHeaders(connectResult.getHeaders());

        response.setDownloadLocation(request.getDownloadLocation());
    }


    private void get(Request request, Response response) {
        ConnectResult connectResult = SimpleHttpUtils.get(request.getUrl(),
                request.getHeaders().getInstance(),
                request.getParams().getInstance(),
                packDownloadPath(request)
        );
        fillResponse(request, connectResult, response);
    }

    private static class RequestBuilder {
        Request template;

        static RequestBuilder init() {
            RequestBuilder requestBuilder = new RequestBuilder();
            requestBuilder.template = new Request();
            return requestBuilder;
        }

        RequestBuilder caller(String caller) {
            this.template.setCaller(caller);
            return this;
        }
        RequestBuilder httpMethod(HttpMethodEnum httpMethod) {
            this.template.setHttpMethod(httpMethod);
            return this;
        }
        RequestBuilder url(String url) {
            this.template.setUrl(url);
            return this;
        }
        RequestBuilder headers(Headers headers) {
            this.template.setHeaders(headers);
            return this;
        }
        RequestBuilder body(Body body) {
            this.template.setBody(body);
            return this;
        }
        RequestBuilder form(Form form) {
            this.template.setForm(form);
            return this;
        }
        RequestBuilder params(Params params) {
            this.template.setParams(params);
            return this;
        }

        RequestBuilder downloadLocation(DownloadLocation downloadLocation) {
            this.template.setDownloadLocation(downloadLocation);
            return this;
        }


        Request build() {
            Request instance = new Request();
            instance.setCaller(template.getCaller());
            instance.setHttpMethod(template.getHttpMethod());
            instance.setUrl(template.getUrl());
            instance.setHeaders(template.getHeaders());
            instance.setBody(template.getBody());
            instance.setForm(template.getForm());
            instance.setParams(template.getParams());
            instance.setDownloadLocation(template.getDownloadLocation());
            instance.setExtendsInfo(new HashMap<>());
            return instance;
        }

    }

    public static RequestTemplate post(String url, Headers headers, Form form) {
        RequestTemplate template = new RequestTemplate();
        if (headers == null) {
            headers = Headers.newHeader();
        }
        if (form == null) {
            form = Form.newForm();
        }
        template.requestBuilder = RequestBuilder.init()
                .url(url)
                .httpMethod(HttpMethodEnum.POST)
                .headers(headers)
                .form(form);
        template.requestInterceptorList = RequestInterceptorRegister.getInterceptor();
        return template;
    }
    public static RequestTemplate post(String url, Headers headers, Body body) {
        RequestTemplate template = new RequestTemplate();
        if (headers == null) {
            headers = Headers.newHeader();
        }
        if (body == null) {
            body = Body.newBody();
        }
        template.requestBuilder = RequestBuilder.init()
                .url(url)
                .httpMethod(HttpMethodEnum.POST)
                .headers(headers)
                .body(body);
        template.requestInterceptorList = RequestInterceptorRegister.getInterceptor();
        return template;
    }
    public static RequestTemplate get(String url, Headers headers, Params params) {
        RequestTemplate template = new RequestTemplate();
        if (headers == null) {
            headers = Headers.newHeader();
        }
        if (params == null) {
            params = Params.newParams();
        }
        template.requestBuilder = RequestBuilder.init()
                .url(url)
                .httpMethod(HttpMethodEnum.GET)
                .headers(headers)
                .params(params);
        template.requestInterceptorList = RequestInterceptorRegister.getInterceptor();
        return template;
    }

    public static RequestTemplate post(String url, Body body) {
        return post(url, null, body);
    }

    public static RequestTemplate get(String url, Params params) {
        return get(url, null, params);
    }

}
