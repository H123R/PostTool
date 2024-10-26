package com.jax.PostTool.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ThrowableUtils {

    public static String stackTraceToString(Throwable cause) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        cause.printStackTrace(printStream);
        printStream.flush();
        try {
            return out.toString();
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {}
        }
    }

}
