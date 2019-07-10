package com.example.yunsystem.service.servicelmpl;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class UploadToHdfsService {

    @Value("${HDFS_PATH}")
    private String HDFS_PATH;
    public void uploadHdfs(String src,String dst)throws Exception{

        Configuration conf = new Configuration();
        System.setProperty("HADOOP_USER_NAME","dc");
        conf.set("fs.defaultFS",HDFS_PATH);
        FileSystem hdfs = FileSystem.get(conf);
        Path srcPath = new Path(src);
        Path dstPath = new Path(dst);
        hdfs.copyFromLocalFile(srcPath,dstPath);

    }
}
