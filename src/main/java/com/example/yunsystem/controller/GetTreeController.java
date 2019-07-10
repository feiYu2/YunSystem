package com.example.yunsystem.controller;

import com.example.yunsystem.service.GetTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
public class GetTreeController {

    @Autowired
    private  GetTreeService getTreeService;

    @PostMapping("/get/tree")
    public List<Map<String,Object>> GetTree(@RequestParam("path") String path) throws IOException, URISyntaxException {
        return getTreeService.FindTree(path);
    }
}
