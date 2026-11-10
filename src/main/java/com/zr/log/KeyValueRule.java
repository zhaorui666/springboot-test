package com.zr.log;

final class KeyValueRule implements MaskRule {

    private static final String FULL_MASK = "******";
    // 词根 contains 匹配：userPassword、password_confirm、accessToken、myPwd 等变形都能命中
    private static final String[] FULL_ROOTS = {"password", "pwd", "token", "secret"};
    // 姓名类保持全词匹配：contains 会误伤 filename 这类正常 key
    private static final String[] NAME_KEYS = {"name", "realname", "username"};

    @Override
    public Span match(String msg, int pos) {
        char c = msg.charAt(pos);
        // 只在标识符 token 起点处理：左边界须非标识符字符，每个 token 只检查一次
        if (!Chars.isAsciiLetter(c) || (pos > 0 && Chars.isKeyChar(msg.charAt(pos - 1)))) {
            return null;
        }
        int n = msg.length();
        // token = 连续字母数字/_/-，覆盖 password_confirm、X-Auth-Token 这类 key
        int tokenEnd = pos + 1;
        while (tokenEnd < n && Chars.isKeyChar(msg.charAt(tokenEnd))) {
            tokenEnd++;
        }
        // 密码类：token 包含任一词根即命中；regionMatches 忽略大小写且零分配
        for (String root : FULL_ROOTS) {
            int rlen = root.length();
            for (int k = pos; k + rlen <= tokenEnd; k++) {
                if (msg.regionMatches(true, k, root, 0, rlen)) {
                    return parseValue(msg, tokenEnd, false);
                }
            }
        }
        // 姓名类：token 与关键字完全相等才命中
        int tokenLen = tokenEnd - pos;
        for (String key : NAME_KEYS) {
            if (key.length() == tokenLen && msg.regionMatches(true, pos, key, 0, tokenLen)) {
                return parseValue(msg, tokenEnd, true);
            }
        }
        return null;
    }

    // 解析关键字后的值：空白/闭合引号 → 分隔符（= : is）→ 按包裹形态取值，任何一步不满足都不命中
    private static Span parseValue(String msg, int keyEnd, boolean nameCategory) {
        int n = msg.length();
        int p = keyEnd;
        // key 与分隔符之间允许空白和 key 的闭合引号，兼容 JSON 写法 "password" : "xxx"
        while (p < n && Chars.isSpace(msg.charAt(p))) {
            p++;
        }
        if (p < n && (msg.charAt(p) == '"' || msg.charAt(p) == '\'')) {
            p++;
        }
        while (p < n && Chars.isSpace(msg.charAt(p))) {
            p++;
        }
        if (p >= n) {
            return null;
        }
        char sep = msg.charAt(p);
        if (sep == ':' || sep == '=') {
            p++;
        } else if ((sep == 'i' || sep == 'I') && p + 1 < n
                && (msg.charAt(p + 1) == 's' || msg.charAt(p + 1) == 'S')
                && (p + 2 == n || Chars.isSpace(msg.charAt(p + 2)) || msg.charAt(p + 2) == '[')) {
            // 兼容 "password is [value]" 的自然语言写法；is 后必须是空白/[ 或结尾，防止误吞 island 等单词
            p += 2;
        } else {
            return null;
        }
        while (p < n && Chars.isSpace(msg.charAt(p))) {
            p++;
        }
        if (p >= n) {
            return null;
        }
        char open = msg.charAt(p);
        int vs;
        int ve;
        if (open == '"' || open == '\'' || open == '[') {
            // 包裹值：取到闭合符号为止，掩码只替换内容，保留外面的引号/方括号
            char close = open == '[' ? ']' : open;
            vs = p + 1;
            ve = vs;
            while (ve < n && msg.charAt(ve) != close) {
                ve++;
            }
        } else {
            // 裸值：取到空白或 , ; & } 等分隔符为止，兼容 key=value 混排和 URL 查询串
            vs = p;
            ve = vs;
            while (ve < n && !Chars.isValueDelimiter(msg.charAt(ve))) {
                ve++;
            }
        }
        // 空值不掩码
        if (ve <= vs) {
            return null;
        }
        // 姓名类保留首字（张**），密码/token 类整值打码且不泄露长度
        String replacement = nameCategory ? msg.charAt(vs) + "**" : FULL_MASK;
        return new Span(vs, ve, replacement);
    }
}