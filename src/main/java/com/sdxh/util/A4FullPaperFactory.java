package com.sdxh.util;

import org.springframework.stereotype.Component;

import java.awt.print.Paper;

/**
 * A4纸工厂
 */
@Component
public class A4FullPaperFactory implements PaperFactory {
    @Override
    public Paper getPaper() {
        return new A4FullPaper();
    }
}
