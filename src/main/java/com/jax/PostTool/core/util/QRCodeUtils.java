package com.jax.PostTool.core.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 二维码工具
 *
 * @author jax
 * @date 2021/9/15 16:17
 */
@Slf4j
public class QRCodeUtils {

    private static final int WIDTH = 254;
    private static final int HEIGHT = 254;

    public static byte[] parseText(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        ByteArrayOutputStream outputStream = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);
            outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            log.error("二维码转换失败，e:{}", ThrowableUtils.stackTraceToString(e));
            throw new RuntimeException(e);
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("二维码转换关流失败，e:{}", ThrowableUtils.stackTraceToString(e));
                }
            }
        }
    }

}
