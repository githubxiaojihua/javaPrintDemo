package com.sdxh.util;

import java.awt.print.Paper;
import java.io.File;

/**
 * 打印机抽象
 */
public interface Printer {
    void printByFile(File file, Paper paper) throws Exception;//根据File进行打印
    void printByByteArray(byte[] bytes, Paper paper) throws Exception;//根据二进制数组进行打印
    void printByFilePath(String filePath, Paper paper) throws Exception;//根据文件路径进行打印
}
