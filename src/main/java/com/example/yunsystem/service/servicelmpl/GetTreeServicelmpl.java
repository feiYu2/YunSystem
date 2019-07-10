package com.example.yunsystem.service.servicelmpl;

import com.example.yunsystem.service.GetTreeService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetTreeServicelmpl implements GetTreeService {

    @Value("${HDFS_PATH}")
    private String HdfsPath;
    //更改文件服务器地址


    @Override
    public List<Map<String, Object>> FindTree(String path) throws URISyntaxException, IOException {

        FileSystem hdfs = null;
        Configuration config = new Configuration();

        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path newpath = new Path(path); //确定搜索文件夹
        List<Map<String, Object>> dirMap = new ArrayList<>();

        ChildDir(hdfs, newpath, dirMap);
        for (int i = 0; i < dirMap.size(); i++) {
            dirMap.get(i).put("leaf", GrandDir(hdfs, new Path((String) dirMap.get(i).get("path"))));
        }

        return dirMap;
    }

    public void ChildDir(FileSystem hdfs, Path path, List<Map<String, Object>> listMap) throws IOException {
        try{
            if(hdfs == null || path == null){
                return;
            }
            //获取文件列表
            FileStatus[] files = hdfs.listStatus(path);

            //展示文件信息
            for (int i = 0; i < files.length; i++) {
                try{
                    if(files[i].isDirectory()){
                        Map<String, Object> list = new HashMap<>();
                        list.put("dirName", files[i].getPath().getName());
                        String suffix = files[i].getPath().toString().substring(files[i].getPath().toString().indexOf("9000") + 4);
                        list.put("path", suffix);
                        System.out.println(">>>" + files[i].getPath()
                                + ", dir owner:" + files[i].getOwner());
                        if(!files[i].getPath().getName().equals("tmp")){
                            listMap.add(list);
                        }
                        //递归调用
                        ChildDir(hdfs, files[i].getPath(),listMap);
                    }else if(files[i].isFile()){
//                        Map<String, Object> list = new HashMap<>();
//                        list.put("filename", files[i].getPath().getName());
//                        String suffix = files[i].getPath().toString().substring(files[i].getPath().toString().indexOf("9000") + 4);
//                        list.put("path", suffix);
//                        listMap.add(list);
                        System.out.println("filename>>>" + files[i].getPath()
                                + ", length:" + files[i].getLen()
                                + ", owner:" + files[i].getOwner());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean GrandDir(FileSystem hdfs, Path path) throws IOException {

        FileStatus[] files = hdfs.listStatus(path);

        for (int k = 0; k < files.length; k++) {
            if (files[k].isDirectory()) {
                return false;
            }
        }
        return true;
    }

}
