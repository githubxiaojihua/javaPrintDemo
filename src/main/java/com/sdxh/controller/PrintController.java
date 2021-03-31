package com.sdxh.controller;

import com.sdxh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import java.io.File;

@RequestMapping("/printBill")
@RestController
public class PrintController {

    @Autowired
    @Qualifier("pdfPrinterFactory")
    PrinterFactory printerFactory;

    @Autowired
    @Qualifier("a4HalfPaperFactory")
    PaperFactory paperFactory;

    @Autowired
    @Qualifier("a4FullPaperFactory")
    PaperFactory a4FullPaperFactory;

    /**
     * PDF打印---按文件名打印  A4
     * @param filePath
     * @return
     */
    @PostMapping("/pdf-FilePath")
    public Result printPdfByFilePath(String filePath) {
        Result result = new Result();
        File file = new File(filePath);
        try {
            printerFactory.getPrinter().printByFile(file,paperFactory.getPaper());
            result.setCode(200);
            result.setMessage("打印成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("打印失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * PDF打印---按base64字符串打印   A4
     * @param base64Str
     * @return
     */
    @PostMapping("/pdf-base64")
    public Result printPdfBybase64(@RequestBody Base64Str base64Str) {
        Result result = new Result();
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(base64Str.getBase64Str());
            printerFactory.getPrinter().printByByteArray(bytes,paperFactory.getPaper());
            result.setCode(200);
            result.setMessage("打印成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("打印失败：" + e.getMessage());
            return result;
        }
    }

    /**
     * PDF打印---按文件名打印  A4
     * @param filePath
     * @return
     */
    @PostMapping("/pdf-A4FullFilePath")
    public Result a4FullprintPdfByFilePath(String filePath) {
        Result result = new Result();
        File file = new File(filePath);
        try {
            printerFactory.getPrinter().printByFile(file,a4FullPaperFactory.getPaper());
            result.setCode(200);
            result.setMessage("打印成功");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("打印失败：" + e.getMessage());
            return result;
        }
    }
}
