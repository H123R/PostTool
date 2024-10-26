package com.jax.PostTool.core.util;

import com.jax.PostTool.core.FormItem;
import com.jax.PostTool.core.Headers;
import com.jax.PostTool.core.constant.HttpMethodEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class SimpleHttpUtils {

    private final static Charset defaultCharset = StandardCharsets.UTF_8;
    private final static int defaultTimeout = 300000;
    /**
     * 请求时指定的分隔符
     */
    private final static String DEFAULT_BOUNDARY = "7d1e128abc0e66ba";
    /**
     * 表单每行的结尾
     */
    private final static String LINE_END = "\r\n";
    /**
     * 使用时每个表单的起始分隔符
     */
    private final static String BOUNDARY_START = "--" + DEFAULT_BOUNDARY + LINE_END;
    /**
     * 使用时表单的结束分隔符
     */
    private final static String BOUNDARY_FINAL = "--" + DEFAULT_BOUNDARY + "--" + LINE_END;



    private static ConnectResult connect(String urlStr,
                                         String method,
                                         Map<String, String> headersMap,
                                         String body,
                                         Map<String, FormItem> formItemMap,
                                         String downloadPath) {
        try {
            // 创建URL对象
            URL url = new URL(urlStr);
            // 打开连接
            long step1 = System.nanoTime();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            long step2 = System.nanoTime();
            // 设置请求方法
            connection.setRequestMethod(method);
            connection.setReadTimeout(defaultTimeout);
            connection.setConnectTimeout(defaultTimeout);
            // 设置请求头信息，例如内容类型
            headersMap.forEach(connection::setRequestProperty);
            if (Objects.equals(method, HttpMethodEnum.POST.name())) {
                // 发送POST请求必须设置
                connection.setDoOutput(true);
                // 获取输出流并写入请求体
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                writeRequestBody(wr, body, formItemMap);
                wr.flush();
                wr.close();
            }
            long step3 = System.nanoTime();
            // 连接
            connection.connect();
            // 读取响应
            StringBuilder response = new StringBuilder();
            int responseCode = connection.getResponseCode();
            long step4 = System.nanoTime();
            if (downloadPath != null) {
                downResponse(connection, response, downloadPath);
            } else {
                readResponse(connection, response);
            }

            long step5 = System.nanoTime();
            // 构造返回数据
            ConnectResult connectResult = new ConnectResult();
            connectResult.setDurationOpenConnect(step2 - step1);
            connectResult.setDurationSendRequest(step3 - step2);
            connectResult.setDurationReceiveResponse(step4 - step3);
            connectResult.setDurationDownloadResponse(step5 - step4);
            connectResult.setResponseCode(responseCode);
            connectResult.setResponseStr(response.toString());
            Headers headers = new Headers();
            connection.getHeaderFields().forEach((k, v) -> {
                headers.header(k, String.join(",", v));
            });
            connectResult.setHeaders(headers);
            return connectResult;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void downResponse(HttpURLConnection connection, StringBuilder response, String downloadPath) {
        try (OutputStream outputStream = Files.newOutputStream(Paths.get(downloadPath))) {
            InputStream inputStream = connection.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                outputStream.flush();
            }
        } catch (IOException e) {
            response.append(ThrowableUtils.stackTraceToString(e));
        }
    }

    private static void readResponse(HttpURLConnection connection, StringBuilder response) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
        } catch (IOException ex) {
            response.append(ex.getMessage());
        }
    }

    private static void writeRequestBody(DataOutputStream wr,
                                         String body,
                                         Map<String, FormItem> formItemMap) throws Exception {
        if (null != formItemMap) {
            writeForm(wr, formItemMap);
        } else {
            writeBody(wr, body);
        }
    }

    private static void writeForm(DataOutputStream wr, Map<String, FormItem> formItemMap) throws Exception {
        for (String key : formItemMap.keySet()) {
            FormItem formItem = formItemMap.get(key);
            wr.writeBytes(BOUNDARY_START);
            if (formItem.getFormType() == FormItem.FormType.FILE) {
                writeFormFile(wr, key, formItem);
            } else if (formItem.getFormType() == FormItem.FormType.STRING) {
                writeForm(wr, key, formItem);
            }
        }
        wr.writeBytes(BOUNDARY_FINAL);
    }

    private static void writeFormFile(DataOutputStream wr, String key, FormItem formItem) throws Exception {
        // 上传文件
        String fileLocalName = formItem.getValue().substring(formItem.getValue().lastIndexOf(File.separator) + 1);
        String fileReqName = key;
        wr.writeBytes("Content-Disposition: form-data; name=\"" + fileReqName + "\"; filename=\"" + fileLocalName + "\"");
        wr.writeBytes(LINE_END);
        wr.writeBytes(LINE_END);

        byte[] buffer = new byte[4096];
        int bytesRead;
        try (FileInputStream uploadInputStream = new FileInputStream(formItem.getValue())) {
            while ((bytesRead = uploadInputStream.read(buffer)) != -1) {
                wr.write(buffer, 0, bytesRead);
            }
        }
        wr.writeBytes(LINE_END);
        wr.writeBytes(LINE_END);
    }

    private static void writeForm(DataOutputStream wr, String key, FormItem formItem) throws Exception {
        wr.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"");
        wr.writeBytes(LINE_END);
        wr.writeBytes(LINE_END);
        wr.writeBytes(formItem.getValue());
        wr.writeBytes(LINE_END);
        wr.writeBytes(LINE_END);
    }

    private static void writeBody(DataOutputStream wr, String body)  throws Exception {
        // json请求体
        wr.write(body.getBytes(defaultCharset));
    }

    public static ConnectResult get(String url,
                                        Map<String, String> headersMap,
                                        Map<String, String> uriParam,
                                        String downloadPath) {
        StringBuilder addParamsUrl = new StringBuilder();
        addParamsUrl.append(url);
        if (!MapUtils.isEmpty(uriParam)) {
            addParamsUrl.append("?");
            Iterator<String> iterator = uriParam.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String val = uriParam.get(key);
                addParamsUrl.append(key);
                addParamsUrl.append("=");
                addParamsUrl.append(val);
                if (iterator.hasNext()) {
                    addParamsUrl.append("&");
                }
            }
        }
        url = addParamsUrl.toString();
        headersMap.put("Accept", "*/*");
        return connect(url,
                HttpMethodEnum.GET.name(),
                headersMap,
                null,
                null,
                downloadPath);
    }

    public static ConnectResult postByJson(String url,
                                         Map<String, String> headersMap,
                                         String body,
                                         String downloadPath) {
        headersMap.put("Content-Type", "application/json;charset=UTF-8");
        headersMap.put("Accept", "*/*");
        return connect(url,
                HttpMethodEnum.POST.name(),
                headersMap,
                body,
                null,
                downloadPath);
    }

    @SneakyThrows
    public static ConnectResult postByForm(String url,
                                         Map<String, String> headersMap,
                                         Map<String, FormItem> formItemMap,
                                           String downloadPath) {
        headersMap.put("Content-Type", "multipart/form-data; boundary=" + DEFAULT_BOUNDARY);
        headersMap.put("Accept", "*/*");
        return connect(url,
                HttpMethodEnum.POST.name(),
                headersMap,
                null,
                formItemMap,
                downloadPath);
    }

}
