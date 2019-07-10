package com.example.yunsystem.controller;

import com.example.yunsystem.service.AdminService;
import com.example.yunsystem.util.JsonResult;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.example.yunsystem.util.JsonUser2Adm;

import java.awt.print.Pageable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
public class AdminController {

    @Value("${HDFS_PATH}")
    private String HDFS_PATH;


    @Autowired
    private AdminService adminService;


    /*查询指定用户信息*/
    @PostMapping("/specialDisplay")
    public JsonResult specialDisplay(@RequestParam String email) throws URISyntaxException{
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus("查找成功");
        jsonResult.setResult(adminService.specificDisply(email));
        return jsonResult;
    }

    /*登录上来就可以查看到的用户列表*/
    @PostMapping("/infoDisplayPage")
    public JsonResult InfoDisplay(@RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        JsonResult jsonResult = new JsonResult();
        PageRequest pageable =  PageRequest.of(currentPage,pageSize);
        jsonResult.setResult(adminService.UserDisplay(pageable));
        return jsonResult;
    }


    /*登录上来就可以查看到的用户列表*/
    @PostMapping("/infoDisplay")
    public JsonResult InfoDisplay() throws URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(adminService.InfoDisplay());
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }


    /*查看用户详细信息*/
    @PostMapping("/userInfoDisplay")
    public JsonResult UserInfoDisplay(@RequestParam String email){
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(adminService.userInfoDisplay(email));
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }


    /*查看vip用户信息*/
    @PostMapping("/vipInfoDisplay")
    public JsonResult VipInfoDisplay() throws IOException,URISyntaxException{
        JsonResult jsonResult = new JsonResult();
        List<JsonUser2Adm> jsonUser2Adms= adminService.vipInfoDisPlay();
        jsonResult.setResult(jsonUser2Adms);
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }


   /*查看非vip用户信息*/
    @PostMapping("/noVipInfoDisplay")
    public JsonResult NoVipInfoDisplay() throws IOException,URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        List<JsonUser2Adm> jsonUser2Adms= adminService.novipInfoDisPlay();
        jsonResult.setResult(jsonUser2Adms);
        jsonResult.setStatus("查看成功");
        return jsonResult;
    }


    /*给用户设置vip权限*/
    @PostMapping("/tobeVip")
    public int tobeVip(@RequestParam String email){
        return adminService.tobeVip(email);
    }


    /*给用户取消vip权限*/
    @PostMapping("/cancelVip")
    public int cancelVip(@RequestParam String email){
        return adminService.cancleVip(email);
    }


    /*给用户启用状态，用户正常使用*/
    @PostMapping("/modeEnable")
    public int modeEnable(@RequestParam String email){
        return adminService.modeEnable(email);
    }


    /*给用户冻结状态，用户不能正常使用*/
    @PostMapping("/modeFreeze")
    public int modeFreeze(@RequestParam String email){
        return adminService.modeFreeze(email);
    }


    /*提交申诉，改变申诉状态，添加申诉信息*/
    @PostMapping("/comSub")
    public String comSub(@RequestParam String email, @RequestParam String cominfo){
        return adminService.comSub(email, cominfo);
    }


    /*管理员查看申诉信息*/
    @PostMapping("/comView")
    public JsonResult comSub(@RequestParam String email){
        JsonResult Result = new JsonResult();
        Result.setStatus("查看成功");
        Result.setResult(adminService.comView(email));
        return Result;
    }


    /*管理员驳回*/
    @PostMapping("/comReturn")
    public int comReturn(@RequestParam String email){
        return adminService.modeFreeze(email);
    }

    /*清空文件夹*/
    @PostMapping("/timedEmptying")
    public void TimedEmptying(@RequestParam String path) {
        final long PERIOD_DAY = 24 * 60 * 60 * 1000;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date=calendar.getTime();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Configuration conf=new Configuration();
                try{
                    URI uri = new URI(HDFS_PATH);
                    FileSystem hdfs = FileSystem.get(uri,conf);
                    Path delef = new Path(path);
                    //递归删除
                    hdfs.delete(delef,true);
                }catch (Exception e){
                    e.getMessage();
                }
            }
        }, date.getTime(),PERIOD_DAY);
    }

}
