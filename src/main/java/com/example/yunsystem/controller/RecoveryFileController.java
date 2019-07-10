package com.example.yunsystem.controller;

import com.example.yunsystem.dao.RecoveryFileDao;
import com.example.yunsystem.service.FileService;
import com.example.yunsystem.util.GlobalFunction;
import com.example.yunsystem.util.Result;
import com.example.yunsystem.entry.RecoveryFile;
import com.example.yunsystem.service.CopyFileService;
import com.example.yunsystem.service.RecoveryFileService;
import com.example.yunsystem.util.ResultUtil;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class RecoveryFileController {
    @Autowired
    private RecoveryFileService recoveryFileService;

    @Autowired
    private CopyFileService copyFileService;
    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private FileService fileService;




    /*获取回收站所有文件*/
    @GetMapping("/user/recycle/get")
    public Result getRecycleFile(String email){
       // email ="1293136689@qq.com";
        String username =email.substring(0,email.indexOf("@"));
        List<RecoveryFile> recoveryFiles = recoveryFileService.findByUsername(username);
        return ResultUtil.success(recoveryFiles);
    }


    /*将文件移动到回收站*/
    @PostMapping("/user/recycle/insert/all")
    public Result moveToRecycleBin(String[] oriPaths,String email) throws IOException {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String username = email.substring(0, email.indexOf("@"));
        String dstPath = "/" + username + "bin/";//该用户回收站目录
        for (String oriPath : oriPaths) {
            recoveryFileService.MoveToRecovery(oriPath, dstPath);
            String filename = oriPath.substring(oriPath.lastIndexOf("/") + 1);
            String check = dstPath + filename;
            RecoveryFile recoveryFile = recoveryFileService.findByPresentPath(check);
            if (recoveryFile == null) {
                recoveryFile = new RecoveryFile();
            }
            //将文件或目录源路径插入数据库
            if (oriPath.lastIndexOf(".") == -1) {
                recoveryFile.setType("folder");
            } else {
                String suffixName = oriPath.substring(oriPath.lastIndexOf(".") + 1);
                String fileType = globalFunction.getFileType(suffixName);
                recoveryFile.setType(fileType);
            }
            Path path = new Path(dstPath + filename);
            FileStatus file = globalFunction.getHadoopFileSystem().getFileStatus(path);
            recoveryFile.setLen(file.getLen());
            recoveryFile.setDelTime(formatter.format(file.getModificationTime()));
            recoveryFile.setSize(globalFunction.getFileSize(file.getLen()));
            recoveryFile.setOriginalPath(oriPath);
            recoveryFile.setUsername(username);
            recoveryFile.setPresentPath(dstPath + filename);
            recoveryFile.setFileName(filename);
            recoveryFileService.insert(recoveryFile);
        }
        return ResultUtil.success();
    }


    /*回收站单个文件还原*/
    @PostMapping("/user/recycle/restore")
    public Result restoreRecycleFile(Long id) throws IOException {
        RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
        String presentPath  = recoveryFile.getPresentPath();
        String originalPath = recoveryFile.getOriginalPath();
        if(copyFileService.checkIsExist(originalPath) == false) {
            recoveryFileService.MoveToRecovery(presentPath, originalPath);
            recoveryFileService.deleteRecoveryFile(id);
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"还原失败，文件（夹）已存在");
        }
    }

    /*回收站多个文件还原*/
    @PostMapping("/user/recycle/restore/all")
    public Result restoreRecycleFile(Long[] ids) throws IOException {
        for(Long checkId : ids) {
            RecoveryFile checkRecoveryFile = recoveryFileService.findByRecoveryId(checkId);
            String checkOriginalPath = checkRecoveryFile.getOriginalPath();
            if(copyFileService.checkIsExist(checkOriginalPath) == true) {
                return ResultUtil.error(1,"还原失败，文件（夹）已存在");
            }
        }
        for(Long id : ids) {
            RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
            String presentPath = recoveryFile.getPresentPath();
            String originalPath = recoveryFile.getOriginalPath();
            recoveryFileService.MoveToRecovery(presentPath, originalPath);
            recoveryFileService.deleteRecoveryFile(id);
        }
        return ResultUtil.success();
    }

    /*回收站删除文件或目录(单个) */
    @PostMapping("/user/recycle/delete")
    public Result deleteRecycleFile(Long id) throws IOException ,URISyntaxException{
        RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
        String presentPath = recoveryFile.getPresentPath();
        boolean b = recoveryFileService.deleteFile(presentPath);
        recoveryFileService.deleteRecoveryFile(id);
        if (b == true){
            return ResultUtil.success();
        }else {
            return ResultUtil.error(1,"删除失败");
        }
    }

    /*删除多个文件*/
    @PostMapping("/user/recycle/delete/all")
    public Result deleteRecycleFile(Long[] ids) throws IOException ,URISyntaxException{
        for(Long id : ids) {
            RecoveryFile recoveryFile = recoveryFileService.findByRecoveryId(id);
            String presentPath = recoveryFile.getPresentPath();
            recoveryFileService.deleteFile(presentPath);
            recoveryFileService.deleteRecoveryFile(id);
        }
        return ResultUtil.success();
    }

    /*清空回收站*/
    @PostMapping("/user/delete/all")
    public Result deleteAllRecycleFile(String email) throws IOException, URISyntaxException {
        String username =email.substring(0,email.indexOf("@"));
        List<RecoveryFile> recoveryFiles = recoveryFileService.findByUsername(username);
        for(int i=0; i<recoveryFiles.size(); i++){
            String presentPath = recoveryFiles.get(i).getPresentPath();
           // fileService.deleteHDFS(presentPath);
            recoveryFileService.deleteFile(presentPath);
            recoveryFileService.deleteRecoveryFile(recoveryFiles.get(i).getRecoveryId());
        }
        return ResultUtil.success();
    }
}
