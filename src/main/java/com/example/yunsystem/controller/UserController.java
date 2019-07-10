package com.example.yunsystem.controller;

import com.example.yunsystem.entry.UserInfor;
import com.example.yunsystem.util.JsonResult;
import com.example.yunsystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.yunsystem.service.UserService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    protected UserService IUserService;

    //主动注册
    @PostMapping(value = "/userLogin")
    public String userLogin(@RequestParam String name, @RequestParam String email,
                          @RequestParam String password,@RequestParam String  code ) throws IOException{

        return IUserService.userLogin(name.trim(),email.trim(),password.trim(),code.trim());
    }
    //输入邮箱获取验证码
    @PostMapping(value = "/mailGet")
    public String mail( @RequestParam String email,@RequestParam String status){

        /*忘记密码*/
        if(status.equals("forget password"))
        {
            /*邮箱存在*/
            if(IUserService.existEmail(email.trim())) {
                return IUserService.sendEmail(email.trim(),"forget password");
            }
            else {
                return "该邮箱不存在";
            }
        }
        /*注册*/
        if(status.equals("register"))
        {
            if(IUserService.existEmail(email.trim())) {
                return "该邮箱已经注册";
            }
            else{
                return IUserService.sendEmail(email.trim(),status); //发送邮件获取验证码
            }
        }
        return "error";

    }

    //忘记密码
    @PostMapping(value = "/forgetPassword")
    public String forgetPassword( @RequestParam String email, @RequestParam String password,@RequestParam String code){
        return IUserService.forgetPassword(email.trim(),password.trim(),code.trim());

    }

    //登录
    @PostMapping(value = "/request_user_login")
    public Result request_user_login(@RequestParam String email, @RequestParam String password){
        return IUserService.user_login(email.trim(),password.trim());
    }

    //登出
    @PostMapping(value = "/request_user_logout")
    public String request_user_logout(@RequestParam String email){
        return IUserService.user_logout(email.trim());
    }

    //修改用户信息
    @PostMapping(value = "/changeInforName")
    public String changeInforName(@RequestParam String email,@RequestParam String name)throws IOException {
        // 项目在容器中实际发布运行的根路径
        return IUserService.changeInforName(email.trim(),name.trim());
    }

    //修改用户信息
    @PostMapping(value = "/changeInforLogo")
    public String changeInforLogo( String email, MultipartFile file)throws IOException {
        // 项目在容器中实际发布运行的根路径
        return IUserService.changeInforLogo(email.trim(),file);
    }

    //查看用户信息
    @PostMapping(value ="/view_user_information")
    public UserInfor view_user_information(@RequestParam String email ){

        return IUserService.view_user_information(email.trim());
    }

    @GetMapping(value = "/getLogo")
    public void getLogo(@RequestParam String email, HttpServletResponse response)throws  IOException{
        IUserService.getLogo(email,response);

    }
}
