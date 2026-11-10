package com.zr.log;

final class DigitRunRule implements MaskRule {

    @Override
    public Span match(String msg, int pos) {
        char c = msg.charAt(pos);
        // 只在数字串起点分类：左边界须非字母数字，排除 abc138... 这类单号；
        // 串中数字因左边界是数字直接返回，保证整条消息仍是单趟 O(n)
        if (!Chars.isDigit(c) || (pos > 0 && Chars.isAsciiAlnum(msg.charAt(pos - 1)))) {
            return null;
        }
        // 一口气扫到数字串结尾，长度和右边界决定命中哪条规则
        int n = msg.length();
        int end = pos + 1;
        while (end < n && Chars.isDigit(msg.charAt(end))) {
            end++;
        }
        int len = end - pos;
        // 特例先行：17 位数字 + X/x 是身份证，必须先于银行卡判断（17 位落在银行卡长度区间内）
        if (len == 17 && end < n && (msg.charAt(end) == 'X' || msg.charAt(end) == 'x')
                && (end + 1 == n || !Chars.isAsciiAlnum(msg.charAt(end + 1)))) {
            return new Span(pos, end + 1, keepEnds(msg, pos, end + 1, 3, 4));
        }
        // 右边界是字母数字，说明是更长编号的一部分（如 13812345678abc），不命中
        if (end < n && Chars.isAsciiAlnum(msg.charAt(end))) {
            return null;
        }
        // 按长度+前缀分类：11 位且 1[3-9] 开头 → 手机号，保留前 3 后 4
        if (len == 11 && c == '1' && msg.charAt(pos + 1) >= '3' && msg.charAt(pos + 1) <= '9') {
            return new Span(pos, end, keepEnds(msg, pos, end, 3, 4));
        }
        // 18 位纯数字 → 身份证，保留前 3 后 4
        if (len == 18) {
            return new Span(pos, end, keepEnds(msg, pos, end, 3, 4));
        }
        // 16~19 位 → 银行卡，保留前 4 后 4
        if (len >= 16 && len <= 19) {
            return new Span(pos, end, keepEnds(msg, pos, end, 4, 4));
        }
        return null;
    }

    // 保留前 prefix 位和后 suffix 位，中间统一打 *（如手机号 138****5678）
    private static String keepEnds(String msg, int start, int end, int prefix, int suffix) {
        char[] buf = new char[end - start];
        msg.getChars(start, start + prefix, buf, 0);
        for (int k = prefix; k < end - start - suffix; k++) {
            buf[k] = '*';
        }
        msg.getChars(end - suffix, end, buf, end - start - suffix);
        return new String(buf);
    }
}