package com.zr.log;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class SensitiveMaskerTest {

    @Test
    public void maskPhone() {
        assertEquals("138****5678", SensitiveMasker.mask("13812345678"));
        assertEquals("呼叫138****5678谢谢", SensitiveMasker.mask("呼叫13812345678谢谢"));
    }

    @Test
    public void maskIdCard() {
        assertEquals("110***********1234", SensitiveMasker.mask("110101199001011234"));
        assertEquals("110***********123X", SensitiveMasker.mask("11010119900101123X"));
    }

    @Test
    public void maskBankCard() {
        assertEquals("6222********1234", SensitiveMasker.mask("6222021234561234"));
        assertEquals("6222***********0123", SensitiveMasker.mask("6222021234567890123"));
    }

    @Test
    public void maskEmail() {
        assertEquals("z***@example.com", SensitiveMasker.mask("zhangsan@example.com"));
        assertEquals("138****5678@163.com", SensitiveMasker.mask("13812345678@163.com"));
    }

    @Test
    public void maskSensitiveKeyValue() {
        assertEquals("password=******", SensitiveMasker.mask("password=abc123"));
        assertEquals("Password:******", SensitiveMasker.mask("Password:ABC"));
        assertEquals("{\"password\":\"******\"}", SensitiveMasker.mask("{\"password\":\"abc123\"}"));
        assertEquals("{\"token\": \"******\"}", SensitiveMasker.mask("{\"token\": \"abc-xyz-123\"}"));
        assertEquals("token=******,userId=5", SensitiveMasker.mask("token=abc123,userId=5"));
        assertEquals("password is [******]", SensitiveMasker.mask("password is [116117]"));
        assertEquals("password is ******", SensitiveMasker.mask("password is abc"));
        assertEquals("PASSWORD IS [******]", SensitiveMasker.mask("PASSWORD IS [abc]"));
    }

    @Test
    public void maskKeyVariantsByRoot() {
        assertEquals("mypassword=******", SensitiveMasker.mask("mypassword=abc"));
        assertEquals("userPassword=******", SensitiveMasker.mask("userPassword=abc"));
        assertEquals("password_confirm=******", SensitiveMasker.mask("password_confirm=abc"));
        assertEquals("accessToken=******", SensitiveMasker.mask("accessToken=abc123"));
        assertEquals("X-Auth-Token: ******", SensitiveMasker.mask("X-Auth-Token: abc"));
        assertEquals("{\"userPassword\":\"******\"}", SensitiveMasker.mask("{\"userPassword\":\"abc\"}"));
        // contains 匹配的已知误伤：含词根的正常 key 也会被打码，宁误杀不泄露
        assertEquals("tokenizer=******", SensitiveMasker.mask("tokenizer=abc"));
    }

    @Test
    public void maskNameKeyValue() {
        assertEquals("name=张**", SensitiveMasker.mask("name=张三"));
        assertEquals("\"realName\":\"张**\"", SensitiveMasker.mask("\"realName\":\"张三丰\""));
        assertEquals("username=z**", SensitiveMasker.mask("username=zhaorui"));
        assertEquals("name is [张**]", SensitiveMasker.mask("name is [张三]"));
    }

    @Test
    public void maskMixedMessage() {
        assertEquals("手机138****5678 身份证110***********1234 邮箱z***@example.com 密码password=******",
                SensitiveMasker.mask("手机13812345678 身份证110101199001011234 邮箱zhangsan@example.com 密码password=abc123"));
    }

    @Test
    public void keepUnmatchedMessage() {
        String plain = "plain message without sensitive data";
        assertSame(plain, SensitiveMasker.mask(plain));
        assertEquals("12345", SensitiveMasker.mask("12345"));
        assertEquals("138123456789", SensitiveMasker.mask("138123456789"));
        assertEquals("order=abc13812345678", SensitiveMasker.mask("order=abc13812345678"));
        assertEquals("password island=abc", SensitiveMasker.mask("password island=abc"));
        assertEquals("password=", SensitiveMasker.mask("password="));
        // 姓名类是全词匹配，filename 不会被 name 误伤
        assertEquals("filename=abc", SensitiveMasker.mask("filename=abc"));
    }

    @Test
    public void nullAndEmpty() {
        assertNull(SensitiveMasker.mask(null));
        assertEquals("", SensitiveMasker.mask(""));
    }
}