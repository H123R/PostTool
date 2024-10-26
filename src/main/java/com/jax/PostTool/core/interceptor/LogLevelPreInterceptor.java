package com.jax.PostTool.core.interceptor;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import com.jax.PostTool.core.Request;
import com.jax.PostTool.core.constant.ResourcePathConstants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;


@Slf4j
public class LogLevelPreInterceptor implements RequestInterceptor {

    @Override
    public int getOrder() {
        return -1000;
    }

    @Override
    public void preRequest(Request request) {
        // 设置日志级别
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(Level.INFO);


        PatternLayoutEncoder patternLayoutEncoder = new PatternLayoutEncoder();
        patternLayoutEncoder.setPattern("%d{HH:mm:ss.SSS}[%-5level] %m%n");
        patternLayoutEncoder.setCharset(StandardCharsets.UTF_8);
        patternLayoutEncoder.setContext(loggerContext);
        patternLayoutEncoder.start();

        ConsoleAppender<ILoggingEvent> console = (ConsoleAppender<ILoggingEvent>) rootLogger.getAppender("console");
        console.setEncoder(patternLayoutEncoder);

        // 将日志追加至日志文件
        FileAppender<ILoggingEvent> appender = new FileAppender<>();
        appender.setName("file");
        appender.setContext(loggerContext);
        appender.setFile(ResourcePathConstants.REQUEST_LOG);
        appender.setEncoder(patternLayoutEncoder);

        appender.start();
        rootLogger.addAppender(appender);
    }

}
