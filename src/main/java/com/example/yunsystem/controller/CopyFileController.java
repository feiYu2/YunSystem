package com.example.yunsystem.controller;



import com.example.yunsystem.util.Result;
import com.example.yunsystem.service.CopyFileService;
import com.example.yunsystem.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin  //解决跨域问题
public class CopyFileController {
    @Autowired
    private CopyFileService copyFileService;

    /**将srcPath文件夹考到dstPath文件夹中*/
    @PostMapping("/user/file/copy")
    public Result copyFile(String srcPath, String dstPath) throws Exception {
        boolean data = copyFileService.copyDir(srcPath,dstPath);
        if (data == true) {
            return ResultUtil.success();
        }
        return ResultUtil.error(1,"文件（夹）已存在");
    }

    /*将srcpaths文件拷到dstpath中*/
    @PostMapping("/user/file/copy/all")
    public Result copyAllFile(String[] srcPaths, String dstPath) throws IOException {
        for (String srcPath:srcPaths) {
            String filename = srcPath.substring(srcPath.lastIndexOf("/") + 1);
            String checkName = dstPath + "/" + filename;
            if(srcPath.equals(dstPath)){
                return ResultUtil.error(2,"复制失败，不能将文件复制到自身或其子目录下");
            }
            if (dstPath.startsWith(srcPath.substring(0,srcPath.length()))){
                return ResultUtil.error(2,"复制失败，不能将文件复制到自身或其子目录下");
            }
            if(copyFileService.checkIsExist(checkName) == true) {
                return ResultUtil.error(1,"复制失败，文件（夹）已存在");
            }
        }

        for (String srcPath:srcPaths){
            boolean data = copyFileService.copyDir(srcPath,dstPath);
        }
        return  ResultUtil.success();
    }
}
