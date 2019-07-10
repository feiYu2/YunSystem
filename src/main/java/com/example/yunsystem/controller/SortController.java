package com.example.yunsystem.controller;

import com.example.yunsystem.entry.UserStore;
import com.example.yunsystem.util.Result;
import com.example.yunsystem.service.SearchService;
import com.example.yunsystem.service.SortService;
import com.example.yunsystem.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@RestController
public class SortController {

    @Autowired
    private SortService sortService;

    @Autowired
    private SearchService searchService;

    @PostMapping("/sort")
    public Result SortFile(Integer flag,String email) throws IOException, URISyntaxException {

        List<Map<String,Object>> list = sortService.SortFile(flag,email);
        return ResultUtil.success(list);

    }

    @PostMapping("/sort/capacity")
    public UserStore SortCapacity(String email) throws IOException, URISyntaxException {
          return sortService.SortCapacity(email);
    }

    @PostMapping("/search/file")
    public Result SearchFile(@RequestParam("searchWord") String SearchWord,@RequestParam("email") String email) throws IOException, URISyntaxException {
        return ResultUtil.success(searchService.SearchFile(SearchWord,email));
    }


}
