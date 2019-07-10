package com.example.yunsystem.service.servicelmpl;

import com.example.yunsystem.dao.UserInforDao;
import com.example.yunsystem.dao.UserStoreDao;
import com.example.yunsystem.dao.repository.UserStoreRespository;
import com.example.yunsystem.entry.User;
import com.example.yunsystem.util.GlobalFunction;
import com.example.yunsystem.util.JsonSort;
import com.example.yunsystem.entry.UserStore;
import com.example.yunsystem.service.SortService;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class SortServicelmpl implements SortService {
    @Autowired
    UserStoreRespository userStoreRepository;
    @Autowired
    UserStoreDao userStoreDao;


    @Autowired
    private UserInforDao userInforDao;

    @Value("${HDFS_PATH}")
    private String HdfsPath;

    @Override
    public List<Map<String, Object>> SortFile(int flag,String email) throws URISyntaxException, IOException {

        /**************  确定用户文件夹  *******************/
        String name = email.substring(0,email.indexOf("@"));
        /*************************************************/
        FileSystem hdfs = null;
        Configuration config = new Configuration();

        config.set("fs.default.name", HdfsPath);
        hdfs = FileSystem.get(new URI(HdfsPath), config);
        Path path = new Path("/" + name);/**********确定用户文件夹  new Path("/" + name)***********/
        List<Map<String, Object>> ListMap = new ArrayList<>();

        //文件分类
        String[] doc = new String[]{"docx", "doc", "xlsx", "xls", "pptx", "ppt", "txt", "pdf", "c"};
        String[] pict = new String[]{"jpg", "png", "gif", "jpeg", "bmp", "JPG"};
        String[] video = new String[]{"avi", "mov", "mp4", "wmv", "mkv", "flv"};
        String[] music = new String[]{"wav", "mp3", "wma", "aac", "flac", "ram", "m4a"};
        String[] other = new String[]{
                "docx", "doc", "xlsx", "xls", "pptx", "ppt", "txt", "pdf",
                "jpg", "png", "gif", "jpeg", "JPG",
                "avi", "mov", "mp4", "wmv", "mkv", "flv",
                "wav", "mp3", "wma", "aac", "flac"};

        ShowFile(hdfs, path, ListMap, flag, doc, pict, video, music, other);
        return ListMap;
    }
    @Override
    public void ShowFile(FileSystem hdfs, Path path,
                         List<Map<String, Object>> ListMap, int flag,
                         String[] doc, String[] pict, String[] video,
                         String[] music, String[] other) throws IOException {

        GlobalFunction globalFunction = new GlobalFunction();

        FileStatus[] files = hdfs.listStatus(path);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //展示文件信息
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                //递归调用
                ShowFile(hdfs, files[i].getPath(), ListMap, flag, doc, pict, video, music, other);

            }
            if (files[i].isFile()) {

                String name = files[i].getPath().getName();
                String suffix = name.substring(name.lastIndexOf(".") + 1);

                Map<String, Object> list = new HashMap<>();
                list.put("fileName", files[i].getPath().getName());

                String truePath = files[i].getPath().toString().substring(files[i].getPath().toString().indexOf("9000") + 4);
                list.put("path", truePath);
                list.put("time", formatter.format(files[i].getModificationTime()));
                list.put("size", globalFunction.getFileSize(files[i].getLen()));
                list.put("len", files[i].getLen());
                list.put("type", globalFunction.getFileType(suffix));

                if (flag == 1) {
                    for (int j = 0; j < doc.length; j++) {
                        if (suffix.equals(doc[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 2) {
                    for (int j = 0; j < pict.length; j++) {
                        if (suffix.equals(pict[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 3) {
                    for (int j = 0; j < video.length; j++) {
                        if (suffix.equals(video[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 4) {
                    for (int j = 0; j < music.length; j++) {
                        if (suffix.equals(music[j])) {
                            ListMap.add(list);
                            break;
                        }
                    }
                }
                if (flag == 5) {
                    int k = 0;
                    for (int j = 0; j < other.length; j++) {

                        if (!suffix.equals(other[j])) {
                            k++;
                        }
                    }
                    if (k == other.length) {
                        ListMap.add(list);
                    }

                }
            }
        }
    }

    @Override
    public UserStore SortCapacity(String email) throws IOException, URISyntaxException {

        GlobalFunction globalFunction = new GlobalFunction();
        String username = email.substring(0,email.indexOf("@"));

        List<UserStore> user = userStoreDao.findByName(email);
        String size = globalFunction.getDirectorySize("/" + username);
        long Lsize = Long.parseLong(size);
        String Ssize = globalFunction.getFileSize(Lsize);


        UserStore userStore = new UserStore();
        userStore.setId(user.get(0).getId());
        userStore.setUsername(user.get(0).getUsername());
        userStore.setDir(user.get(0).getDir());
        userStore.setAvailableCapacity(user.get(0).getAvailableCapacity());
        userStore.setUsedCapacity(Long.toString(Lsize));
        userStoreDao.update(userStore);


        return userStore;
    }

}
