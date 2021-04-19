package com.javaprint.util.printer;

import com.javaprint.util.Result;

import java.awt.print.Paper;
import java.io.File;

/**
 * 打印机抽象
 */
public interface Printer {
    Result printByFile(File file, Paper paper);//根据File进行打印
    Result printByByteArray(byte[] bytes, Paper paper);//根据二进制数组进行打印
    Result printByFilePath(String filePath, Paper paper);//根据文件路径进行打印
}
