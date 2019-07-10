package com.example.yunsystem.service;

import com.example.yunsystem.entry.UserStore;
import com.example.yunsystem.util.JsonSort;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface SortService {
    List<Map<String, Object>> SortFile(int flag,String email) throws URISyntaxException, IOException;

    void ShowFile(FileSystem hdfs, Path path, List<Map<String, Object>> ListMap, int flag,
                  String[] doc, String[] pict, String[] video,
                  String[] music, String[] other) throws IOException;

    UserStore SortCapacity(String email) throws IOException, URISyntaxException;

}
