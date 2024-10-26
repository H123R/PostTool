package com.jax.localTools.common;

import com.jax.PostTool.core.constant.ResourcePathConstants;
import com.jax.PostTool.core.util.QRCodeUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.io.FileOutputStream;

public class QRCode {

    @SneakyThrows
    @Test
    public void content2QRCode() {
        // （优先）将文本生成二维码
        String content = "http://www.github.com";
        // 将文本转成Base64格式后，再生成二维码
        String contentBase64  = "";

        byte[] res;
        if (StringUtils.isNotBlank(content)) {
            res = QRCodeUtils.parseText(content);
        } else {
            res = QRCodeUtils.parseText(Base64Utils.encodeToString(contentBase64.getBytes()));
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(ResourcePathConstants.LOCAL_TOOLS_DIR + "QRCode.png");
            outputStream.write(res);
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }

    }

}
