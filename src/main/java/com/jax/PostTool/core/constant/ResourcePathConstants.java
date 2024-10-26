package com.jax.PostTool.core.constant;

import java.io.File;

public class ResourcePathConstants {

    public static final String REQUEST_DIR = ClasspathConstants.RESOURCES_PATH
            + File.separator
            + "request"
            + File.separator;
    public static final String RESPONSE_DIR = ClasspathConstants.RESOURCES_PATH
            + File.separator
            + "response"
            + File.separator;
    public static final String UPLOAD_FILE_DIR = REQUEST_DIR + "upload" + File.separator;
    public static final String DOWNLOAD_FILE_DIR = RESPONSE_DIR + "download" + File.separator;
    public static final String REQUEST_LOG = REQUEST_DIR + "request.log";
    public static final String RESPONSE_JSON = RESPONSE_DIR + "response.json";
    public static final String LOCAL_TOOLS_DIR = ClasspathConstants.RESOURCES_PATH
            + File.separator
            + "localTools"
            + File.separator;

}
