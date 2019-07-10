package com.example.yunsystem.controller;

import com.example.yunsystem.service.FileService;
import com.example.yunsystem.util.JsonResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;


@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    @Autowired
    private FileService fileService;

    //   file 文件名 mulupath 文件路径 email 邮箱
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<JsonResult> upload(@RequestParam("file") MultipartFile file, @RequestParam String mulupath,@RequestParam String email) throws URISyntaxException {
       return fileService.upload(file, mulupath,email);
    }

    //   pPath 创建该文件的父目录全路径
    //   fileName 创建该文件的文件名
    @RequestMapping(value = "/mkdir", method = RequestMethod.POST)
    public JsonResult mkdirMulu(@RequestParam String pPath, @RequestParam String fileName,@RequestParam String email) throws URISyntaxException {
        return fileService.mkdirMulu(pPath,fileName,email);
    }

    //   muluNam要查看文件夹的全路径
    @RequestMapping(value = "/dirLook", method = RequestMethod.POST)
    public JsonResult lookdir(@RequestParam String muluName) throws URISyntaxException {
        return fileService.lookdir(muluName);
    }

    //    重命名文件夹  新旧文件夹的全路径
    //         oldPath hdfs文件的旧路径
    //          newName文件的新的名字
    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public JsonResult rename(@RequestParam String oldPath, @RequestParam String newName) throws URISyntaxException{
        return fileService.rename(oldPath, newName);
    }

    //     删除文件 fileDelPaths文件全路径
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public List<JsonResult> deleteHDFS(@RequestParam String[] fileDelPaths) throws URISyntaxException {
        System.out.println("删除文件");
        return fileService.deleteHDFS(fileDelPaths);
    }



     //  oldDirPaths 要移动的文件的路径
    //    newFatherPath 要移动到的文件夹的路径
    @RequestMapping(value = "/move", method = RequestMethod.POST)
    public List<JsonResult> move(@RequestParam String[] oldDirPaths , @RequestParam String newFatherPath) throws URISyntaxException{
        return fileService.move(oldDirPaths, newFatherPath);
    }

    @PostMapping("/decompress")
    public  JsonResult decompress(@RequestParam String path) throws URISyntaxException{
        JsonResult jsonResult = new JsonResult();
        String fileName = path.substring(path.lastIndexOf("/")+1);
        String  suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        if(suffix.equals("gz")||suffix.equals("bz2")||suffix.equals("lzo")||suffix.equals("deflate")||suffix.equals("LZ4")||suffix.equals("snappy")){
            fileService.decompress(path);
            jsonResult.setStatus("解压成功");
            jsonResult.setResult("解压成功");
        }else{
            jsonResult.setStatus("解压失败");
            jsonResult.setResult("暂时不支持此格式");
        }
        return jsonResult;
    }
}


