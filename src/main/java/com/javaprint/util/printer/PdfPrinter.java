package com.javaprint.util.printer;

import com.javaprint.util.Result;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Sides;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;

/**
 * Pdf打印机
 */
@Component
public class PdfPrinter implements  Printer{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Result printByFile(File file, Paper paper){
        PDDocument document = null;
        Result scussR = new Result();
        scussR.setCode(200);
        scussR.setMessage("打印成功");

        try {
            document = PDDocument.load(file);
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setJobName(file.getName());
            PrintService[] printServices = PrinterJob.lookupPrintServices();
            if(printServices == null || printServices.length == 0) {
                throw new RuntimeException("打印失败，未找到可用打印机，请检查。");
            }
            PrintService printService = printServices[0];
            printJob.setPrintService(printService);

            //设置纸张及缩放
            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            //设置多页打印
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            //设置打印方向
            pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
            pageFormat.setPaper(paper);//设置纸张
            book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
            printJob.setPageable(book);
            printJob.setCopies(1);//设置打印份数
            //添加打印属性
            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
            pars.add(Sides.DUPLEX); //设置单双页
            printJob.print(pars);
            return scussR;
        }catch (Exception e){
            Result failR = new Result();
            failR.setCode(400);
            failR.setMessage("打印失败");
            failR.setData(e);
            return failR ;
        }finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public Result printByByteArray(byte[] bytes, Paper paper) {
        PDDocument document = null;
        Result scussR = new Result();
        scussR.setCode(200);
        scussR.setMessage("打印成功");
        try {
            document = PDDocument.load(bytes);
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setJobName("电子票据打印");
            PrintService[] printServices = PrinterJob.lookupPrintServices();
            if(printServices == null || printServices.length == 0) {
                System.out.print("打印失败，未找到可用打印机，请检查。");
            }
            PrintService printService = printServices[0];
            //设置纸张及缩放
            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            //设置多页打印
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            //设置打印方向
            pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
            pageFormat.setPaper(paper);//设置纸张
            book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
            printJob.setPageable(book);
            printJob.setCopies(1);//设置打印份数
            //添加打印属性
            HashPrintRequestAttributeSet pars = new HashPrintRequestAttributeSet();
            pars.add(Sides.DUPLEX); //设置单双页
            printJob.print(pars);

            return scussR;
        }catch (Exception e){
            Result failR = new Result();
            failR.setCode(400);
            failR.setMessage("打印失败");
            failR.setData(e);
            return failR ;
        }finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Result printByFilePath(String filePath, Paper paper) {
        File file = new File(filePath);
        return this.printByFile(file,paper);
    }

}
