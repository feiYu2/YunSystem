package com.example.yunsystem.service.servicelmpl;


import com.example.yunsystem.dao.Md5Dao;
import com.example.yunsystem.util.GlobalFunction;
import com.example.yunsystem.util.Constants;
import com.example.yunsystem.entry.Md5;
import com.example.yunsystem.entry.RecoveryFile;
import com.example.yunsystem.dao.repository.Md5Repository;
import com.example.yunsystem.dao.repository.RecoveryFileRepository;
import com.example.yunsystem.service.RecoveryFileService;
import com.example.yunsystem.util.JsonResult;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RecoveryFileServiceImpl implements RecoveryFileService {

    @Value("${HDFS_PATH}")
    private String HDFS_PATH;


    @Autowired
    private Md5Dao md5Dao;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private GlobalFunction globalFunction;
    @Autowired
    private Md5Repository md5Repository;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RecoveryFileRepository recoveryFileRepository;


    public boolean MoveToRecovery(String oriPath,String dstPath) throws IOException {
        FileSystem fs = globalFunction.getHadoopFileSystem();


        boolean b = false;
        Path oldPath = new Path(oriPath);
        Path newPath = new Path(dstPath);

        try {
            b = fs.rename(oldPath,newPath);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }finally {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        String oldname = oriPath.substring(oriPath.lastIndexOf("/") + 1);
        String oldFatherPath = oriPath.substring(0,oriPath.length()-oldname.length()-1);
        String newFatherPath = dstPath.substring(0,dstPath.length()-1);
        /*文件的新hdfs全路径*/
        String newDirPath = dstPath + oldname;
        //先判断当前路径是否是文件夹
        Md5 IsDir = md5Repository.findByUidAndPathAndFileName("文件夹",oldFatherPath,oldname);
        if (IsDir != null){
            //是文件夹，修改文件夹的路径
            int i = md5Repository.updateDirPath(newFatherPath,"文件夹",oldFatherPath,oldname);
            List<Md5> byPathLike = md5Repository.findByPathLike(oriPath);//找出所有在该文件夹下的文件
            for (Md5 md5:byPathLike){
                String newFilePath  = newFatherPath+ md5.getPath().substring(oldFatherPath.length());
                md5Repository.updateFilePathInDirNameLike(newFilePath,md5.getPath());
            }
        }
        else {//是文件，修改该条数据的路径
            int i = md5Repository.updateFilePath(newFatherPath, oldFatherPath, oldname);
        }
        return b;
    }

    @Override
    public boolean deleteFile(String filePath) throws IOException ,URISyntaxException{
        //获取文件系统
        JsonResult jsonResult = new JsonResult();

        String deleteName = filePath;
        Path deletePath = new Path(deleteName);
        Configuration conf = new Configuration();
        URI uri = new URI(HDFS_PATH);
        FileSystem fileSystem;
        try {
            fileSystem = FileSystem.get(uri, conf);
            if (!fileSystem.exists(deletePath)) {
                jsonResult.setStatus("文件不存在");
            } else {
                List<Md5> file = new ArrayList<>();
                String path = filePath.substring(0,filePath.lastIndexOf("/"));
                String filename = filePath.substring(filePath.lastIndexOf("/")+1);
                file = md5Dao.findByFileNameAndPath(filename,path);
                if(file.size()!=0){
                    md5Dao.detele(file.get(0).getId());
                }

                fileSystem.delete(deletePath, true);
                jsonResult.setStatus("删除成功！");
            }
            jsonResult.setResult(deleteName);
        } catch (IOException e) {
            e.printStackTrace();
            jsonResult.setResult(e.getMessage());
            jsonResult.setStatus("删除失败！");
        }
        System.out.println(jsonResult.getStatus() + ": " + jsonResult.getResult());
        return  true;
    }

    @Override
    public RecoveryFile insert(RecoveryFile recoveryFile) {
        return recoveryFileRepository.save(recoveryFile);
    }

    @Override
    public void deleteRecoveryFile(Long id) {
        recoveryFileRepository.deleteById(id);
    }

    @Override
    public List<RecoveryFile> findByUsername(String username) {
        return recoveryFileRepository.findByUsername(username);
    }

    @Override
    public RecoveryFile findByRecoveryId(Long recoveryId) {
        return recoveryFileRepository.findByRecoveryId(recoveryId);
    }

    @Override
    public RecoveryFile findByPresentPath(String presentPath) {
        return recoveryFileRepository.findByPresentPath(presentPath);
    }
}
