package com.example.yunsystem.service;

import com.example.yunsystem.util.JsonResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;

public interface FileService {
    void decompress(String path) throws URISyntaxException;
    JsonResult mkdirMulu(String pPath, String fileName,String email) throws URISyntaxException;
    JsonResult lookdir(String muluName) throws URISyntaxException;
    JsonResult rename( String oldPath, String newName) throws URISyntaxException;
    List<JsonResult> deleteHDFS(String[] fileDelPaths) throws URISyntaxException;
    List<JsonResult> move(String[] oldDirPaths , String newFatherPath) throws URISyntaxException;
    ResponseEntity<JsonResult> upload(MultipartFile file, String mulupath,String email) throws URISyntaxException;
}
