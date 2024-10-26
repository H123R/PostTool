package com.jax.PostTool.core.interceptor;

import com.jax.PostTool.core.Body;
import com.jax.PostTool.core.Form;
import com.jax.PostTool.core.FormItem;
import com.jax.PostTool.core.Headers;
import com.jax.PostTool.core.Params;
import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.Response;
import com.jax.PostTool.core.env.EnvironmentManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderInterceptor implements RequestInterceptor {

    private static final String PLACEHOLDER_REGEX = "(#\\{[a-zA-Z0-9\\-_ ,=]+\\})";

    @Override
    public int getOrder() {
        return -10;
    }

    @Override
    public boolean enable(Request request, Response response) {
        return true;
    }

    @Override
    public void preRequest(Request request) {
        String url = request.getUrl();
        request.setUrl((String) replace(url));

        Headers headers = request.getHeaders();
        for (String key : headers.getInstance().keySet()) {
            String val = headers.getInstance().get(key);
            headers.getInstance().put(key, (String) replace(val));
        }

        Body body = request.getBody();
        if (body != null) {
            String bodyStr = body.getBodyStr();
            String newBodyStr = (String) replace(bodyStr);
            body.resetBody(newBodyStr);
        }

        Form form = request.getForm();
        if (form != null) {
            for (String key : form.getInstance().keySet()) {
                FormItem formItem = form.getInstance().get(key);
                if (StringUtils.isNotBlank(formItem.getValue())) {
                    formItem.setValue((String) replace(formItem.getValue()));
                }
            }
        }

        Params params = request.getParams();
        if (null != params) {
            for (String key : params.getInstance().keySet()) {
                String val = params.getInstance().get(key);
                params.getInstance().put(key, (String) replace(val));
            }
        }

    }

    public Object replace(Object obj) {
        if (obj instanceof String) {
            String objStr = (String) obj;
            Pattern pattern = Pattern.compile(PLACEHOLDER_REGEX);
            Matcher matcher = pattern.matcher(objStr);
            List<String> matchedList = new ArrayList<>();
            while (matcher.find()) {
                String matched = matcher.group();
                matchedList.add(matched);
            }
            for (String matched : matchedList) {
                String propertyName = splitPlaceholder(matched);
                objStr = objStr.replace(matched, EnvironmentManager.getProperty(propertyName));
            }
            return objStr;
        }
        return obj;
    }

    private String splitPlaceholder(String matched) {
        return matched.substring(2, matched.length() - 1);
    }

}
