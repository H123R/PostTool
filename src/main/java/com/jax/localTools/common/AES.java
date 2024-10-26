package com.jax.localTools.common;

import com.jax.PostTool.core.util.AESUtils;
import lombok.SneakyThrows;
import org.junit.Test;

public class AES {

    /**
     * AES解密
     */
    @SneakyThrows
    @Test
    public void decrypt() {
        String key = "";
        String content = "";
        String decrypt = AESUtils.decrypt(content, key);
        System.out.println(decrypt);
    }

}
