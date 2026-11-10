package com.zr.log;

interface MaskRule {

    // 从 pos 位置尝试匹配，未命中返回 null；规则无状态，只读 msg
    Span match(String msg, int pos);
}