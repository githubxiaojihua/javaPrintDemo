package com.sdxh.util;

import org.springframework.stereotype.Component;

/**
 * PDF打印机工厂
 */
@Component
public class PdfPrinterFactory implements PrinterFactory{
    @Override
    public Printer getPrinter() {
        return new PdfPrinter();
    }
}
