package com.example.yunsystem.controller;


import com.example.yunsystem.dao.UserInforDao;
import com.example.yunsystem.entry.User;
import com.example.yunsystem.service.SortService;

import com.example.yunsystem.util.Constants;
import com.example.yunsystem.util.GlobalFunction;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin  //解决跨域问题
public class ImageController {
    @Autowired
    private GlobalFunction globalFunction;

    @Autowired
    private SortService sortService;
    @Autowired
    private UserInforDao userInforDao;

    @Value("${HDFS_PATH}")
    private String HADOOP_URL;

    /*点击图片预览*/
    @GetMapping("/usr/img/preview")
    public void imagePreview(String imagePath, HttpServletResponse response) throws IOException {

        String filename = HADOOP_URL + imagePath;
        //获取文件系统
        FileSystem fs = globalFunction.getHadoopFileSystem();

        //获取路径
        Path p = new Path(filename);
        //通过文件系统打开路径获取HDFS文件输入流
        FSDataInputStream fis = fs.open(p);
        //创建缓冲区
        byte[] buf = new byte[fis.available()];
        int len = -1;
        //当当读取的长度不等于-1的时候开始写入
        //写入需要字节输出流
        OutputStream baos = response.getOutputStream();
        while ((len = fis.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        //写入完毕，关闭输入流
        fis.close();
        //关闭输出流
        baos.close();
    }

    /*获取图片列表*/
    @GetMapping("/usr/img/line")
    public List<Map<String, Object>> SortFile(String email) throws IOException, URISyntaxException {

        FileSystem fs = globalFunction.getHadoopFileSystem();
        // 读取图片字节数组
        List<Map<String, Object>> dataList = new ArrayList<>();

        List<Map<String, Object>> list = sortService.SortFile(2,email);
        //取出path所有值
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            String imgPath = map.get("path").toString();
            //获取路径
            Path p = new Path(HADOOP_URL + imgPath);
            //通过文件系统打开路径获取HDFS文件输入流
            FSDataInputStream in = fs.open(p);
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            map.put("base64", encoder.encode(data));
            dataList.add(map);
        }
        return dataList;
    }


    /*预览音频和视频*/
    @GetMapping("/get/stream")
    public void preview(String fpath, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if (fpath == null)
            return;
        String filename = HADOOP_URL + fpath;
        Configuration config = new Configuration();
        FileSystem fs = null;
        FSDataInputStream in = null;
        try {
            fs = FileSystem.get(URI.create(filename), config);
            in = fs.open(new Path(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final long fileLen = fs.getFileStatus(new Path(filename)).getLen();
        String range = req.getHeader("Range");
        resp.setHeader("Content-type", "video/mp3");
        OutputStream out = resp.getOutputStream();
        if (range == null) {
            filename = fpath.substring(fpath.lastIndexOf("/") + 1);
            resp.setHeader("Content-Disposition", "attachment; filename=" + filename);
            resp.setContentType("application/octet-stream");
            resp.setContentLength((int) fileLen);
            IOUtils.copyBytes(in, out, fileLen, false);
        } else {
            long start = Integer.valueOf(range.substring(range.indexOf("=") + 1, range.indexOf("-")));
            long count = fileLen - start;
            long end;
            if (range.endsWith("-"))
                end = fileLen - 1;
            else
                end = Integer.valueOf(range.substring(range.indexOf("-") + 1));
            String ContentRange = "bytes " + String.valueOf(start) + "-" + end + "/" + String.valueOf(fileLen);
            resp.setStatus(206);
            resp.setContentType("video/mpeg3");
            resp.setHeader("Content-Range", ContentRange);
            in.seek(start);
            try {
                IOUtils.copyBytes(in, out, count, false);
            } catch (Exception e) {
                throw e;
            }
        }
        in.close();
        out.close();
    }

    @GetMapping("/get/video")
    public void previewview(String fpath, HttpServletRequest req, HttpServletResponse resp) throws IOException , URISyntaxException{

        if(fpath==null)
            return;
        String filename = HADOOP_URL + fpath;
        Configuration config=new Configuration();
        FileSystem fs=null;
        FSDataInputStream in=null;
        try {
            fs = FileSystem.get(URI.create(filename),config);
            System.out.println("xxxx");
            in=fs.open(new Path(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final long fileLen = fs.getFileStatus(new Path(filename)).getLen();
        String range=req.getHeader("Range");
        resp.setHeader("Content-type","video/mp4");
        OutputStream out=resp.getOutputStream();
        if(range==null)
        {
            filename=fpath.substring(fpath.lastIndexOf("/")+1);
            resp.setHeader("Content-Disposition", "attachment; filename="+filename);
            resp.setContentType("application/octet-stream");
            resp.setContentLength((int)fileLen);
            IOUtils.copyBytes(in, out, fileLen, false);
        }
        else
        {
            long start=Integer.valueOf(range.substring(range.indexOf("=")+1, range.indexOf("-")));
            long count=fileLen-start;
            long end;
            if(range.endsWith("-"))
                end=fileLen-1;
            else
                end=Integer.valueOf(range.substring(range.indexOf("-")+1));
            String ContentRange="bytes "+String.valueOf(start)+"-"+end+"/"+String.valueOf(fileLen);
            resp.setStatus(206);
            resp.setContentType("video/mpeg4");
            resp.setHeader("Content-Range",ContentRange);
            in.seek(start);
            try{
                IOUtils.copyBytes(in, out, count, false);
            }
            catch(Exception e)
            {
                throw e;
            }
        }
        in.close();
        in = null;
        out.close();
        out = null;
    }


    @GetMapping("/icon/get")
    public void getIcon(HttpServletResponse response, String username) throws IOException {
        String path = System.getProperty("user.dir")+"\\logo\\";
        List<User> user =new ArrayList<>();
        user =userInforDao.query(username);
        if(user.size()==0){
            return ;
        }

        byte[] data = null;
        FileImageInputStream input = null;
        String logo = path + user.get(0).getLogo();

        response.setContentType("image/jpeg");//设置输出流内容格式为图片格式
        response.setCharacterEncoding("utf-8");//response的响应的编码方式为utf-8
        try {
            input = new FileImageInputStream(new File(logo));
            OutputStream outputStream = response.getOutputStream();//输出流
            //InputStream in = new ByteArrayInputStream(data);//字节输入流
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = input.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            input.close();
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
