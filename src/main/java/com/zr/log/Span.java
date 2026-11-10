package com.zr.log;

final class Span {

    final int start;
    final int end;
    final String replacement;

    Span(int start, int end, String replacement) {
        this.start = start;
        this.end = end;
        this.replacement = replacement;
    }
}