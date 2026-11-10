package com.zr.log;

final class EmailRule implements MaskRule {

    @Override
    public Span match(String msg, int pos) {
        // 以 @ 为锚点向前找本地部分、向后找域名，避免逐字符猜测邮箱起点
        if (msg.charAt(pos) != '@') {
            return null;
        }
        int n = msg.length();
        // 向前回溯本地部分（字母数字及 . _ % + -），直到遇到非法字符
        int start = pos;
        while (start > 0 && Chars.isLocalPartChar(msg.charAt(start - 1))) {
            start--;
        }
        // 向后扫域名（字母数字及 . -），记录是否含点
        int end = pos + 1;
        boolean hasDot = false;
        while (end < n && Chars.isDomainChar(msg.charAt(end))) {
            if (msg.charAt(end) == '.') {
                hasDot = true;
            }
            end++;
        }
        // 本地部分为空、域名无点或太短（至少 a.b 三段）→ 不是邮箱，排除 @Override 这类注解
        if (start == pos || !hasDot || end <= pos + 2) {
            return null;
        }
        // 保留本地部分首字符和完整域名：zhangsan@example.com → z***@example.com
        return new Span(start, end, msg.charAt(start) + "***" + msg.substring(pos, end));
    }
}