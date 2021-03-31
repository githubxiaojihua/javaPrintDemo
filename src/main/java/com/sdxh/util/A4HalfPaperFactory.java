package com.sdxh.util;

import org.springframework.stereotype.Component;

import java.awt.print.Paper;

/**
 * A4纸 半张 工厂
 */
@Component
public class A4HalfPaperFactory implements PaperFactory {
    @Override
    public Paper getPaper() {
        return new A4HalfPaper();
    }
}
