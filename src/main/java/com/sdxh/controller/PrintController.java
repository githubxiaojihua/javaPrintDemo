package com.sdxh.controller;

import com.sdxh.util.Base64Str;
import com.sdxh.util.Print;
import com.sdxh.util.Result;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import java.io.File;

@RequestMapping("/printBill")
@RestController
public class PrintController {

    /**
     * 按PDF文件名打印
     * @param filePath
     * @return
     */
    @PostMapping
    public Result print(String filePath) {
        Result result = new Result();
        File file = new File(filePath);
        try {
            Print.PDFprint(file);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("打印失败：" + e.getMessage());
        }

        result.setCode(200);
        result.setMessage("打印成功");
        return result;
    }

    /**
     * 按PDF base64字符串打印
     * @param base64Str
     * @return
     */
    @PostMapping("/base64Print")
    public Result base64Print(@RequestBody Base64Str base64Str) {
        Result result = new Result();
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(base64Str.getBase64Str());
            Print.PDFprintByBase64(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("打印失败：" + e.getMessage());
        }
        result.setCode(200);
        result.setMessage("打印成功");
        return result;
    }
}
