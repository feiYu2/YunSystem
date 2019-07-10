package com.example.yunsystem.service.servicelmpl;

import com.example.yunsystem.dao.UserInforDao;
import com.example.yunsystem.dao.UserStoreDao;
import com.example.yunsystem.dao.repository.UserStoreRespository;
import com.example.yunsystem.entry.User;
import com.example.yunsystem.entry.UserInfor;
import com.example.yunsystem.entry.UserStore;
import com.example.yunsystem.service.IconService;
import com.example.yunsystem.service.UserService;
import com.example.yunsystem.service.UserStoreService;
import com.example.yunsystem.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserServicelmpl implements UserService {
    @Autowired
    private UserInforDao userInforDao;

    @Autowired
    JavaMailSender jms;

    @Autowired
    private IconService iconService;

    @Autowired
    private UserStoreDao userStoreDao;


    /*注册用户*/
    @Override
    public String userLogin(String name,String email,String password,String code) throws IOException{
        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            System.out.println("邮箱没有发送验证码?");
            return "error";
        }
        String Code = member.get(0).getCode().toString();
        if(!Code.equals(code)){
            User user = new User();
            user.setId(member.get(0).getId());
            userInforDao.detele(user);
            System.out.println("邮箱验证失败！");
            return "Mailbox Authentication Failed";
        }
        String path = System.getProperty("user.dir")+"\\Data";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String url = email.substring(0,email.indexOf("@"));
        path=path+"\\"+url;
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        User user = new User();
        user.setId(member.get(0).getId());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setCode(0);
        userInforDao.update(user);
        System.out.println(email+":"+"用户注册成功！");
        //在hadoop上创建属于用户的根文件夹并在创建回收站目录
        String username =email.substring(0,email.indexOf("@"));
        String userDirPath = "/" + username;
        boolean result1 =  iconService.createDir(userDirPath);

        UserStore userStore = new UserStore();
        userStore.setUsername(user.getEmail());
        userStore.setDir("/"+username);
        userStore.setAvailableCapacity("2GB");
        userStoreDao.insert(userStore);

        if(result1 == true){
            String userTmpPath = "/" + username+"bin"+"/";
            boolean result2 =  iconService.createDir(userTmpPath);
            if(result2 == true){
                return "success";
            }
        }


        return "error";
    }

    /*判断邮箱是否已经注册*/
    @Override
    public boolean existEmail(String email){
        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if ( member.size()  == 0 )
        {
            System.out.println("邮箱注册失败");
            return false;
        }else{
            System.out.println("This mailbox has been registered!");
            return true;
        }
    }
    @Override
    /*发送邮箱*/
    public String sendEmail(String email,  String test){
        //生成6为随机数
        Random random = new Random();
        int code = random.nextInt(899999) + 100000;
        SimpleMailMessage mainMessage = new SimpleMailMessage();
        //发送者
        mainMessage.setFrom("1726260651@qq.com");
        //接收者
        mainMessage.setTo(email);
        //发送的标题
        mainMessage.setSubject("hello");		//发送的内容
        mainMessage.setText(test+":"+code);
        jms.send(mainMessage);

        if(test.equals("register"))
        {
            User user = new User();
            user.setEmail(email);
            user.setRole(1);
            user.setLogo("1.png");
            user.setCode(code);
            userInforDao.insert(user);
            System.out.println(email+":"+"register");
            return "success";
        }
        else if(test.equals("forget password"))
        {
            /*将验证码存入数据库*/
            List<User> member = new ArrayList<>();
            member = userInforDao.query(email);
            String Code = member.get(0).getCode().toString();
            User user = new User();
            user.setId(member.get(0).getId());
            user.setName(member.get(0).getName());
            user.setEmail(email);
            user.setPassword("111111");
            user.setCode(code);
            userInforDao.update(user);
            System.out.println(email+":"+"forget password");
            return "success";
        }
        System.out.println(email+":"+"forget password or register");
        return "error";
    };

    /*修改密码*/
    @Override
    public String forgetPassword(String email,String password,String code){

        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            return "error";
        }
        String Code = member.get(0).getCode().toString();
        if(Code.equals(code)){
            User user = new User();
            user.setId(member.get(0).getId());
            user.setName(member.get(0).getName());
            user.setEmail(email);
            user.setPassword(password);
            user.setCode(0);
            userInforDao.update(user);
            System.out.println(email+":"+"修改密码成功！");
            return "success";
        }
        System.out.println(email+":"+"修改密码失败！");
        return "error";
    }

    /*用户登录*/
    @Override
    public Result user_login(String email, String password){
        Result result = new Result();
        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            System.out.println(email+":"+"不存在该用户！");
            result.setCode(-1);
            result.setMsg("error");
            return result;
        }
        if(member.get(0).getState().equals(1)||member.get(0).getState().equals(0)){
            if(member.get(0).getPassword().equals(password)) {
                User user = new User();
                user.setId(member.get(0).getId());
                user.setName(member.get(0).getName());
                user.setEmail(member.get(0).getEmail());
                user.setPassword(member.get(0).getPassword());
                user.setRole(member.get(0).getRole());
                user.setLogo(member.get(0).getLogo());
                user.setCode(member.get(0).getCode());
                user.setState(1);
                userInforDao.updateAll(user);
                System.out.println(email + ":" + "用户登录成功！");


                UserInfor userinfo = new UserInfor();
                userinfo.setId(member.get(0).getId());
                userinfo.setName(member.get(0).getName());
                userinfo.setEmail(member.get(0).getEmail());
                userinfo.setPassword("");
                userinfo.setRole(member.get(0).getRole());
                userinfo.setState(member.get(0).getState());
                userinfo.setLogoname(member.get(0).getLogo());
                String path = System.getProperty("user.dir") + "\\logo";
                String filePath = path + "\\";
                byte[] data = null;
                FileImageInputStream input = null;
                String logo = filePath + member.get(0).getLogo();
                try {
                    input = new FileImageInputStream(new File(logo));
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int numBytesRead = 0;
                    while ((numBytesRead = input.read(buf)) != -1) {
                        output.write(buf, 0, numBytesRead);
                    }
                    data = output.toByteArray();
                    output.close();
                    input.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userinfo.setLogo(data);
                result.setMsg("success");
                result.setCode(1);
                result.setData(userinfo);
                return result;
            }else{
                System.out.println(email+":"+"用户登录失败！");
                result.setCode(-1);
                result.setMsg("error");
            }
        }

        member.get(0).setPassword("");
        if(member.get(0).getState().equals(2)){
            result.setCode(-1);
            member.get(0).setPassword("");
            result.setMsg("此账号正在申诉中！");;
        }
        if(member.get(0).getState().equals(3)){
            result.setCode(-1);
            result.setMsg("此账号已冻结！");

        }
        result.setData(member.get(0));
        return result;
    }

    /*用户登出*/
    @Override
    public String user_logout(String email){
        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            return "error";
        }
        if(member.get(0).getState().equals(1)){
            User user = new User();
            user.setId(member.get(0).getId());
            user.setName(member.get(0).getName());
            user.setEmail(member.get(0).getEmail());
            user.setPassword(member.get(0).getPassword());
            user.setRole(member.get(0).getRole());
            user.setLogo(member.get(0).getLogo());
            user.setCode(member.get(0).getCode());
            user.setState(0);
            userInforDao.updateAll(user);
            System.out.println(email+":"+"用户登出成功！");
            return "success";
        }
        System.out.println(email+":"+"用户登出失败！");
        return "error";
    }

    /*修改信息*/
    @Override
    public String changeInforLogo( String email, MultipartFile file) throws IOException {
        String path = System.getProperty("user.dir")+"\\logo";
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            System.out.println(email+":"+"不存在此用户信息！");
            return "error";
        }
        if(!file.isEmpty())    //头像logo需要修改
        {
            //首先获取图片名称的格式（png，jpg）设置logo的名称为id.格式
            String format = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));
            String Filename =member.get(0).getEmail();
            String filename =Filename.substring(0, Filename.indexOf("@"))+format;
            //判断该id用户的头像是否之前存在，如果有删除之前的文件
            String delete_file_name = member.get(0).getLogo();
            if(!delete_file_name.equals("1.png") && delete_file_name!=null )
            {
                File delet_file = new File(path+"/"+delete_file_name);
                if(delet_file.exists())
                {
                    delet_file.delete();
                }
            }
            // 设置文件存储路径
            String down_path = path+"/";
            File dest = new File(down_path + filename);
            if(!dest.getParentFile().exists()){    //父目录不能存在则创建
                dest.getParentFile().mkdir();
            }
            InputStream input = file.getInputStream();
            int index;
            byte[] bytes = new byte[1024];
            FileOutputStream downloadFile = new FileOutputStream(down_path + filename);
            while ((index = input.read(bytes)) != -1) {
                downloadFile.write(bytes, 0, index);
                downloadFile.flush();
            }
            downloadFile.close();
            input.close();


            try{
                file.transferTo(dest);
                User user = new User();
                user.setId(member.get(0).getId());
                user.setName(member.get(0).getName());
                user.setEmail(member.get(0).getEmail());
                user.setPassword(member.get(0).getPassword());
                user.setRole(member.get(0).getRole());
                user.setLogo(filename);
                user.setCode(member.get(0).getCode());
                user.setState(member.get(0).getState());
                System.out.println(email+":"+"用户修改头像成功！");
                userInforDao.updateAll(user);
            } catch (IOException e) {
                e.printStackTrace();
                return "Logofile Load Fail";
            }catch (IllegalStateException e){
                e.printStackTrace();
                return "Logofile Load Fail";

            }
        }
        return "success";
    }

    /*修改信息*/
    @Override
    public String changeInforName( String email, String name) {

        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            System.out.println(email+":"+"不存在此用户信息！");
            return "error";
        }


        User user = new User();
        user.setId(member.get(0).getId());
        user.setName(name);
        user.setEmail(member.get(0).getEmail());
        user.setPassword(member.get(0).getPassword());
        user.setRole(member.get(0).getRole());
        user.setLogo(member.get(0).getLogo());
        user.setCode(member.get(0).getCode());
        user.setState(member.get(0).getState());
        System.out.println(email+":"+"用户修改name成功！");
        userInforDao.updateAll(user);

        return "success";
    }

    /*查看用户信息*/
    @Override
    public UserInfor view_user_information(String email) {
        String path = System.getProperty("user.dir")+"\\logo";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        UserInfor userinfo = new UserInfor();
        List<User> member = new ArrayList<>();
        member = userInforDao.query(email);
        if(member.size()==0){
            System.out.println(email+":"+"用户查看信息失败！");
            userinfo.setId(-1);
            return userinfo;
        }
        userinfo.setId(member.get(0).getId());
        userinfo.setName(member.get(0).getName());
        userinfo.setEmail(member.get(0).getEmail());
        userinfo.setPassword("");
        userinfo.setRole(member.get(0).getRole());
        userinfo.setState(member.get(0).getState());
        userinfo.setLogoname(member.get(0).getLogo());

        String filePath = path+"/";
        byte[] data = null;
        FileImageInputStream input = null;
        String logo = filePath + member.get(0).getLogo();
        try {
            input = new FileImageInputStream(new File(logo));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1){
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        userinfo.setLogo(data);
        System.out.println(email+":"+"用户查看信息成功！");
        return userinfo;
    }

    /*获取头像*/
    @Override
    public void getLogo(String email, HttpServletResponse response)throws IOException{
        String path = System.getProperty("user.dir")+"\\logo\\";
        List<User> user =new ArrayList<>();
        user =userInforDao.query(email);
        if(user.size()==0){
            return ;
        }

        byte[] data = null;
        FileImageInputStream input = null;
        String logo = path + user.get(0).getLogo();

        response.setContentType("image/jpeg");//设置输出流内容格式为图片格式
        response.setCharacterEncoding("utf-8");//response的响应的编码方式为utf-8
        try {
            input = new FileImageInputStream(new File(logo));
            OutputStream outputStream = response.getOutputStream();//输出流
            //InputStream in = new ByteArrayInputStream(data);//字节输入流
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = input.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            input.close();
            outputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}

