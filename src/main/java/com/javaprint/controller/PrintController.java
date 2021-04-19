package com.javaprint.controller;

import com.javaprint.util.*;
import com.javaprint.util.paper.PaperFactory;
import com.javaprint.util.printer.Printer;
import com.javaprint.util.printer.PrinterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import java.io.File;
import java.io.IOException;

@RequestMapping("/printBill")
@RestController
public class PrintController {

    @Autowired
    @Qualifier("pdfPrinterFactory")
    PrinterFactory printerFactory;

    @Autowired
    @Qualifier("newPdfPrinterFactory")
    PrinterFactory newPrinterFactory;

    @Autowired
    @Qualifier("a4HalfPaperFactory")
    PaperFactory paperFactory;

    @Autowired
    @Qualifier("a4FullPaperFactory")
    PaperFactory a4FullPaperFactory;

    @Autowired
    @Qualifier("imagePrinter")
    Printer imagePrinter;

    //======================pdf打印===============
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


    @PostMapping("/newpdf-base64")
    public Result newPrintPdfBybase64(@RequestBody Base64Str base64Str) {
        Result result = new Result();
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = new byte[0];
        try {
            bytes = decoder.decodeBuffer(base64Str.getBase64Str());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = newPrinterFactory.getPrinter().printByByteArray(bytes,paperFactory.getPaper());
        return result;

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

    //========================image打印==============================

    /**
     * Image打印---按文件名打印  A4
     * @param
     * @return
     */
    @PostMapping("/image-base64")
    public Result newPrintImageBybase64(@RequestBody Base64Str base64Str) {
        Result result = new Result();
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes = new byte[0];
        try {
            bytes = decoder.decodeBuffer(base64Str.getBase64Str());
        } catch (IOException e) {
            e.printStackTrace();
        }
        result = imagePrinter.printByByteArray(bytes,paperFactory.getPaper());
        return result;

    }
}
