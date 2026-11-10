package com.zr.log;

// 主循环只做"匹配→拷贝"：规则链逐位置试探，未命中零分配返回原串，命中才创建 StringBuilder
public final class SensitiveMasker {

    private static final MaskRule[] RULES = {new KeyValueRule(), new DigitRunRule(), new EmailRule()};

    private SensitiveMasker() {
    }

    public static String mask(String msg) {
        if (msg == null || msg.isEmpty()) {
            return msg;
        }
        StringBuilder sb = null;
        int last = 0;
        int i = 0;
        int n = msg.length();
        while (i < n) {
            Span span = null;
            for (MaskRule rule : RULES) {
                span = rule.match(msg, i);
                if (span != null) {
                    break;
                }
            }
            // 命中区与已掩码区重叠时跳过（如手机号作邮箱前缀），防止二次掩码越界
            if (span == null || span.start < last) {
                i++;
                continue;
            }
            if (sb == null) {
                sb = new StringBuilder(n);
            }
            sb.append(msg, last, span.start).append(span.replacement);
            last = span.end;
            i = span.end;
        }
        return sb == null ? msg : sb.append(msg, last, n).toString();
    }
}