package com.example.yunsystem.controller;


import com.example.yunsystem.service.PreviewOnlineServcie;
import com.example.yunsystem.util.JsonResult;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@CrossOrigin  //解决跨域问题
public class PreviewOnlineController {
    @Autowired
    private PreviewOnlineServcie  previewOnlineServcie;

    @GetMapping  (value = "/preview/pdf")
    public void showPdf(@RequestParam  String path, HttpServletRequest request, HttpServletResponse response) throws URISyntaxException, DocumentException, IOException{
        response.setContentType("application/pdf");
        String Path = previewOnlineServcie.file2Pdf(path);
        if(Path.equals("下载失败！")){
            return ;
        }
        InputStream in = new FileInputStream(Path);
        OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
        //创建存放文件内容的数组
        byte[] buff =new byte[1024];
        //所读取的内容使用n来接收
        int n;
        //当没有读取完时,继续读取,循环
        while((n=in.read(buff))!=-1){
            //将字节数组的数据全部写入到输出流中
            outputStream.write(buff,0,n);
        }
        //强制将缓存区的数据进行输出
        outputStream.flush();
        //关流
        outputStream.close();
        in.close();

    }

    //取消预览之后，调用此接口
    @PostMapping(value = "/pdfDelete")
    public JsonResult pdfDelete(@RequestParam String deletePath){
        return previewOnlineServcie.pdfDelete(deletePath);
    }

}
