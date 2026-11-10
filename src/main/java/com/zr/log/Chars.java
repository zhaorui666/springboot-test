package com.zr.log;

final class Chars {

    private Chars() {
    }

    static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    static boolean isAsciiLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    static boolean isAsciiAlnum(char c) {
        return isDigit(c) || isAsciiLetter(c);
    }

    // 标识符字符：关键字 token 的组成字符，兼容 password_confirm、X-Auth-Token
    static boolean isKeyChar(char c) {
        return isAsciiAlnum(c) || c == '_' || c == '-';
    }

    static char toLowerAscii(char c) {
        return c >= 'A' && c <= 'Z' ? (char) (c + 32) : c;
    }

    static boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    static boolean isValueDelimiter(char c) {
        return isSpace(c) || c == ',' || c == ';' || c == '&' || c == '}' || c == ']' || c == ')' || c == '>' || c == '|';
    }

    static boolean isLocalPartChar(char c) {
        return isAsciiAlnum(c) || c == '.' || c == '_' || c == '%' || c == '+' || c == '-';
    }

    static boolean isDomainChar(char c) {
        return isAsciiAlnum(c) || c == '.' || c == '-';
    }
}