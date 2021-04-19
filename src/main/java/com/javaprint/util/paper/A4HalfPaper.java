package com.javaprint.util.paper;

import java.awt.print.Paper;

/**
 * A4纸 半页
 */
public class A4HalfPaper extends Paper {
    public A4HalfPaper(){
        // 默认为A4纸张，对应像素宽和高分别为 595, 842
        int width = 595;
        int height = 421;
        // 设置边距，单位是像素，10mm边距，对应 28px
        int marginLeft = 0;
        int marginRight = 5;
        int marginTop = 5;
        int marginBottom = 0;
        this.setSize(width, height);
        // 下面一行代码，解决了打印内容为空的问题
        this.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));
    }
}
