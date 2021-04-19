package com.javaprint.util.printer;

import com.javaprint.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.*;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import java.awt.print.Paper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ImagePrinter implements  Printer{

    Logger logger = LoggerFactory.getLogger(getClass());

    private static ImagePrinter imagePrinter;
    private ImagePrinter(){}
    public static synchronized ImagePrinter getInstance(){
        if(imagePrinter == null){
            imagePrinter = new ImagePrinter();
        }
        return imagePrinter;
    }

    @Override
    public Result printByFile(File file, Paper paper) {
        if (file == null) {
            System.err.println("缺少打印文件");
        }
        InputStream fis = null;
        try {
            // 设置打印格式，如果未确定类型，可选择autosense
            DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
            // 设置打印参数
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new Copies(1)); //份数
            aset.add(MediaSize.ISO.A4); //纸张
            // aset.add(Finishings.STAPLE);//装订
            aset.add(Sides.DUPLEX);//单双面
            // 定位打印服务
            PrintService printService = null;

            //获得本台电脑连接的所有打印机
            PrintService printServices = PrintServiceLookup.lookupDefaultPrintService();


            fis = new FileInputStream(file); // 构造待打印的文件流
            Doc doc = new SimpleDoc(fis, flavor, null);
            DocPrintJob job = printService.createPrintJob(); // 创建打印作业
            job.print(doc, aset);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            // 关闭打印的文件流
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public Result printByByteArray(byte[] bytes, Paper paper) {

        Result scussR = new Result();
        scussR.setCode(200);
        scussR.setMessage("请领取电子票据，若未打印成功可能是打印机缺纸请联系现场服务人员！");

        try {
            // 设置打印格式，如果未确定类型，可选择autosense
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.PNG;

            DocAttributeSet das=new HashDocAttributeSet();
            das.add(OrientationRequested.PORTRAIT);
            das.add(new MediaPrintableArea(1,1,210,148,MediaPrintableArea.MM));
            Doc doc = new SimpleDoc(bytes, flavor, das);
            // 设置打印参数
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(new Copies(1)); //份数
            aset.add(MediaSizeName.ISO_A4); //纸张
            aset.add(Sides.DUPLEX);//单双面

            //获得本台电脑连接的所有打印机
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            DocPrintJob job = printService.createPrintJob(); // 创建打印作业

            job.addPrintJobListener(new PrintJobListener() {
                @Override
                public void printJobRequiresAttention(PrintJobEvent arg0) {
                    logger.info("printJobRequiresAttention");
                }

                @Override
                public void printJobNoMoreEvents(PrintJobEvent arg0) {
                    logger.info("通知客户端,不需要再提供事件");
                }

                @Override
                public void printJobFailed(PrintJobEvent arg0) {
                    logger.info("通知客户端无法完成作业,必须重新提交");

                }

                @Override
                public void printJobCompleted(PrintJobEvent arg0) {
                    logger.info("打印结束");

                }

                @Override
                public void printJobCanceled(PrintJobEvent arg0) {
                    logger.info("作业已被用户或者程序取消");

                }

                @Override
                public void printDataTransferCompleted(PrintJobEvent arg0) {
                    logger.info("数据已成功传输打印机");

                }
            });

            job.print(doc, aset);

        } catch (Exception e1) {
            Result failR = new Result();
            failR.setCode(400);
            failR.setMessage("打印失败");
            failR.setData(e1);
            return failR ;
        } finally {

        }
        return scussR;
    }

    @Override
    public Result printByFilePath(String filePath, Paper paper) {
        return null;
    }


    public static PrinterFactory printerFactory = new PrinterFactory() {
        @Override
        public Printer getPrinter() {
            return ImagePrinter.getInstance();
        }
    };

}
