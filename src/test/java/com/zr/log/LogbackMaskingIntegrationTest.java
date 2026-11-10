package com.zr.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LogbackMaskingIntegrationTest {

    @Test
    public void sensitiveDataMaskedInLogFile() throws Exception {
        Path logFile = Paths.get("target/test-logs/springboot-test.log");
        Files.createDirectories(logFile.getParent());
        Files.deleteIfExists(logFile);

        LoggerContext context = new LoggerContext();
        context.putProperty("LOG_HOME", "target/test-logs");
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(context);
        configurator.doConfigure("src/main/resources/logback.xml");
        try {
            Logger logger = context.getLogger("masking-it");
            logger.info("登录请求 手机13812345678 身份证110101199001011234 邮箱zhangsan@example.com password=abc123 name=张三");
            logger.info("password is [{}]", "116117");
        } finally {
            // 停止 context 让 AsyncAppender 排空队列，确保日志落盘
            context.stop();
        }

        String content = new String(Files.readAllBytes(logFile), StandardCharsets.UTF_8);
        assertTrue(content.contains("138****5678"));
        assertTrue(content.contains("110***********1234"));
        assertTrue(content.contains("z***@example.com"));
        assertTrue(content.contains("password=******"));
        assertTrue(content.contains("name=张**"));
        assertTrue(content.contains("password is [******]"));
        assertFalse(content.contains("116117"));
        assertFalse(content.contains("13812345678"));
        assertFalse(content.contains("110101199001011234"));
        assertFalse(content.contains("zhangsan@example.com"));
        assertFalse(content.contains("abc123"));
        assertFalse(content.contains("张三"));
    }
}