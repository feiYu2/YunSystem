package com.example.yunsystem.service;

import com.example.yunsystem.entry.UserInfor;
import com.example.yunsystem.util.JsonResult;
import com.example.yunsystem.util.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

    String userLogin(String name, String email, String password,String code) throws IOException;
    boolean existEmail(String email);
    String  sendEmail(String email,String test);
    String forgetPassword(String email,String password,String code);
    Result user_login(String email, String password);
    String user_logout(String email);
    String changeInforName(String email, String name) ;
    String changeInforLogo( String email,  MultipartFile logo) throws IOException;
    UserInfor view_user_information(String email);
    void getLogo(String email, HttpServletResponse response)throws IOException;
}
