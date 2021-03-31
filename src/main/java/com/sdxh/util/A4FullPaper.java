package com.sdxh.util;

import java.awt.print.Paper;

/**
 * A4纸
 */
public class A4FullPaper extends Paper {
    public A4FullPaper(){
        // 默认为A4纸张，对应像素宽和高分别为 595, 842
        int width = 595;
        int height = 842;
        // 设置边距，单位是像素，10mm边距，对应 28px
        int marginLeft = 10;
        int marginRight = 0;
        int marginTop = 50;
        int marginBottom = 0;
        this.setSize(width, height);
        // 下面一行代码，解决了打印内容为空的问题
        this.setImageableArea(marginLeft, marginRight, width - (marginLeft + marginRight), height - (marginTop + marginBottom));

    }
}
