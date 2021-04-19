package com.javaprint.util.printer;

import org.springframework.stereotype.Component;

/**
 * PDF打印机工厂-----可报告打印机状态
 */
@Component
public class NewPdfPrinterFactory implements PrinterFactory{
    @Override
    public Printer getPrinter() {
        return new NewPdfPrinter();
    }
}
