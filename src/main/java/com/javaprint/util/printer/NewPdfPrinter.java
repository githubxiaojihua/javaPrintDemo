package com.javaprint.util.printer;

import com.javaprint.util.Result;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.print.PageableDoc;

import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.print.event.PrintServiceAttributeEvent;
import javax.print.event.PrintServiceAttributeListener;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.IOException;

/**
 * 使用新API重构 Pdf打印机
 * 可报告打印机状态
 */
public class NewPdfPrinter implements  Printer{


    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Result printByFile(File file, Paper paper){
        return null;
    }

    @Override
    public Result printByByteArray(byte[] bytes, Paper paper){ ;
        PDDocument document = null;
        Result scussR = new Result();
        scussR.setCode(200);
        scussR.setMessage("请领取电子票据，若未打印成功可能是打印机缺纸请联系现场服务人员！");

        try{
            // 获取默认打印机服务
            PrintService service = this.getPrintService();
            // 创建打印任务并加载打印内容
            DocPrintJob printJob = service.createPrintJob();

            printJob.addPrintJobListener(new PrintJobListener() {
                @Override
                public void printDataTransferCompleted(PrintJobEvent pje) {
                    logger.info("111111111111 " + pje.getPrintEventType());
                }

                @Override
                public void printJobCompleted(PrintJobEvent pje) {
                    logger.info("222222222222 " + pje.getPrintEventType());
                }

                @Override
                public void printJobFailed(PrintJobEvent pje) {
                    logger.info("333333333333 " + pje.getPrintEventType());
                }

                @Override
                public void printJobCanceled(PrintJobEvent pje) {
                    logger.info("444444444444 " + pje.getPrintEventType());
                }

                @Override
                public void printJobNoMoreEvents(PrintJobEvent pje) {
                    logger.info("5555555555555 " + pje.getPrintEventType());
                }

                @Override
                public void printJobRequiresAttention(PrintJobEvent pje) {
                    logger.info("6666666666666 " + pje.getPrintEventType());
                }
            });

            document =  PDDocument.load(bytes);
            //设置打印属性
            PDFPrintable pdfPrintable = new PDFPrintable(document, Scaling.ACTUAL_SIZE);
            Book book = new Book();
            PageFormat pageFormat = new PageFormat();
            pageFormat.setOrientation(PageFormat.PORTRAIT);//纵向
            pageFormat.setPaper(paper);//设置纸张
            book.append(pdfPrintable, pageFormat, document.getNumberOfPages());
            PageableDoc doc = new PageableDoc(book);
            logger.info("========调用打印方法print前的QueuedJobCount属性====" + service.getAttribute(QueuedJobCount.class));

            // 开始打印
            printJob.print(doc,null);

            // 获取打印后的相关状态进行反馈
            PrinterStateReasons printerStateReasons = service.getAttribute(PrinterStateReasons.class);
            logger.info("========调用打印方法print后的QueuedJobCount属性====" + service.getAttribute(QueuedJobCount.class));
            logger.info("========调用打印方法print后的printJob.PrinterIsAcceptingJobs属性====" + printJob.getAttributes().get(PrinterIsAcceptingJobs.class));
            logger.info("========调用打印方法print后的printJob.JobState====" + printJob.getAttributes().toArray()[0].getName());
            logger.info("========调用打印方法print后的printJob.JobState====" + printJob.getAttributes().toArray()[1].getName());

            logger.info("========调用打印方法print后的service.PrinterIsAcceptingJobsnewPrinterIsAcceptingJobs====" + service.getAttribute(PrinterIsAcceptingJobs.class));
            logger.info("========调用打印方法print后的printerStateReasons====" + printerStateReasons);
            logger.info("========打印结束========================");
            QueuedJobCount count = service.getAttribute(QueuedJobCount.class);

//            if(count.getValue() > 1){
//                scussR.setCode(201);
//                scussR.setMessage("打印机缺纸或卡纸，请联系管理员！");
//            }

            return scussR ;

        }catch (Exception e){
            Result failR = new Result();
            failR.setCode(400);
            failR.setMessage("打印失败");
            failR.setData(e);
            return failR ;
        }finally {
            // 关闭资源
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
        return null;
    }

    /**
     * 返回默认打印服务
     * @return
     */
    private PrintService getPrintService(){
        // 获取默认打印机
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        // 设置打印机属性监听器
        service.addPrintServiceAttributeListener(new PrintServiceAttributeListener() {
            public void attributeUpdate(PrintServiceAttributeEvent psae) {
                PrintServiceAttributeSet attribute = psae.getAttributes();
                logger.info("========监听器输出开始====================================");
                logger.info("========更新的属性数量====" + attribute.size());
                logger.info("========属性0的名称====" + attribute.toArray()[0].getName());
                logger.info("========属性1的名称====" + attribute.toArray()[1].getName());
                logger.info("========属性QueuedJobCount的值====" + attribute.get(QueuedJobCount.class) );
                logger.info("========属性PrinterStateReasons的值====" + attribute.get(PrinterStateReasons.class));
                logger.info("========属性JobStateReasons的值====" + attribute.get(JobStateReason.class));
                logger.info("========属性CopiesSupported的值====" + attribute.get(CopiesSupported.class));
                logger.info("========监听器输出结束====================================");
            }
        });
        logger.info("选择的打印机名称：" + service.getName());
        return service;
    }


}
