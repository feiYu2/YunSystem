package com.example.yunsystem.service.servicelmpl;

import com.example.yunsystem.dao.ComplaintDao;
import com.example.yunsystem.dao.UserInforDao;
import com.example.yunsystem.dao.UserStoreDao;
import com.example.yunsystem.dao.repository.ComplaintRepository;
import com.example.yunsystem.dao.repository.ShareDetailsRepository;
import com.example.yunsystem.dao.repository.UserInforRepository;
import com.example.yunsystem.entry.Complaint;
import com.example.yunsystem.entry.User;
import com.example.yunsystem.service.AdminService;
import com.example.yunsystem.util.GlobalFunction;
import com.example.yunsystem.util.JsonUser2Adm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServicelmpl implements AdminService {

    @Autowired
    private ComplaintRepository complaintRepository;
    @Autowired
    private UserInforDao userInforDao;
    @Autowired
    private UserInforRepository userInforRepository;
    @Autowired
    private ComplaintDao complaintDao;

    public List<Integer> User_id(){
        List<User> user = userInforDao.findByRoleId();//普通管理员
        List<Integer> user_id = new ArrayList<>();
        for(int i = 0;i<user.size();i++){
            Integer userid = user.get(i).getId();
            user_id.add(userid);
        }
        return user_id;
    }

    public List<User> users(){
        List<Integer> user_id = User_id();
        List<User> users = new ArrayList<>();
        for (int i=0;i<user_id.size();i++){
            List<User> user = userInforDao.findbyID(user_id.get(i));
            users.add(user.get(0));
        }
        return users;
    }

    @Override
    public List<JsonUser2Adm> InfoDisplay() throws URISyntaxException {
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        List<User> user = users();
        for (int i = 0; i < user.size(); i++) {
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = user.get(i).getEmail();
            username = username.substring(0,username.indexOf("@"));
            List<User> userInfo = userInforDao.query(user.get(i).getEmail());
            jsonUser2Adm.setId(userInfo.get(0).getId());
            jsonUser2Adm.setUsername(userInfo.get(0).getName());
            Integer state = user.get(i).getState();
            jsonUser2Adm.setEmail(userInfo.get(0).getEmail());

            if (state.equals(3)) {
                jsonUser2Adm.setStateStr("未申诉 ");
                jsonUser2Adm.setState(3);
            } else if (state.equals(2)){
                jsonUser2Adm.setStateStr("已申诉");
                jsonUser2Adm.setState(2);
            } else if (state.equals(1)||state.equals(0)){
                jsonUser2Adm.setStateStr("账号正常");
                jsonUser2Adm.setState(state);
            }else {
                jsonUser2Adm.setState(4);
                jsonUser2Adm.setStateStr("已驳回");
            }
            try {
                GlobalFunction globalFunction = new GlobalFunction();

                String size = globalFunction.getDirectorySize("/" + username);
                long Lsize = Long.parseLong(size);
                String Ssize = globalFunction.getFileSize(Lsize);
                jsonUser2Adm.setUsedsize(Ssize);
                Integer vip = userInfo.get(0).getRole();
                if (vip.equals(2)) {
                    jsonUser2Adm.setTotalsize("5 GB");
                    jsonUser2Adm.setRole(2);
                    Float usedsize = (float) Lsize / (5 * 1000 * 1000 * 1000);
                    BigDecimal bb = new BigDecimal(usedsize*100);
                    float f1 = bb.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                    jsonUser2Adm.setUsedInfo(f1);
                } else if(vip.equals(0)){
                    jsonUser2Adm.setTotalsize("10 GB");
                    jsonUser2Adm.setRole(0);
                    Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
                    BigDecimal b = new BigDecimal(usedsize*100);
                    float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                    jsonUser2Adm.setUsedInfo(f1);
                }else{
                    jsonUser2Adm.setTotalsize("2 GB");
                    jsonUser2Adm.setRole(1);
                    Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
                    BigDecimal b = new BigDecimal(usedsize*100);
                    float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                    jsonUser2Adm.setUsedInfo(f1);

                }
            } catch (IOException e) {
                e.getMessage();
            }
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    @Override
    public Page<User> UserDisplay(Pageable pageable) {
        return userInforRepository.findAll(pageable);
    }

    @Override
    public User userInfoDisplay(String email) {
        return userInforDao.query(email).get(0);
    }

    /*查看vip信息*/
    @Override
    public List<JsonUser2Adm> vipInfoDisPlay() throws IOException, URISyntaxException {
        List<User> userInfos = userInforDao.findAllVip();
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = userInfos.get(i).getEmail();
            username=username.substring(0,username.indexOf("@"));
            User user = userInforDao.query(userInfos.get(i).getEmail()).get(0);

            jsonUser2Adm.setId(userInfos.get(i).getId());
            jsonUser2Adm.setRole(2);
            GlobalFunction globalFunction = new GlobalFunction();
            String size = globalFunction.getDirectorySize("/" + username);
            long Lsize = Long.parseLong(size);
            String Ssize = globalFunction.getFileSize(Lsize);
            jsonUser2Adm.setUsedsize(Ssize);
            jsonUser2Adm.setTotalsize("5 GB");
            Float usedsize = (float) Lsize / (5 * 1000 * 1000 * 1000);
            BigDecimal bb = new BigDecimal(usedsize*100);
            float f1 = bb.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            jsonUser2Adm.setUsedInfo(f1);

            jsonUser2Adm.setUsername(username);
            Integer state = user.getState();
            if (state.equals(3)) {
                jsonUser2Adm.setStateStr("未申诉 ");
                jsonUser2Adm.setState(3);
            } else if (state.equals(2)){
                jsonUser2Adm.setStateStr("已申诉");
                jsonUser2Adm.setState(2);
            } else if (state.equals(1)||state.equals(0)){
                jsonUser2Adm.setStateStr("账号正常");
                jsonUser2Adm.setState(state);
            }else {
                jsonUser2Adm.setState(4);
                jsonUser2Adm.setStateStr("已驳回");
            }
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    /*查看非vip信息*/
    @Override
    public List<JsonUser2Adm> novipInfoDisPlay() throws IOException, URISyntaxException {

        List<User> userInfos = userInforDao.findAllNVip();
        List<JsonUser2Adm> jsonUser2Adms = new ArrayList<>();
        for (int i = 0; i < userInfos.size(); i++) {
            JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
            String username = userInfos.get(i).getEmail();
            username = username.substring(0,username.indexOf("@"));
            User user = userInforDao.query(userInfos.get(i).getEmail()).get(0);

            Integer state = user.getState();
            if (state.equals(3)) {
                jsonUser2Adm.setStateStr("未申诉 ");
                jsonUser2Adm.setState(3);
            } else if (state.equals(2)){
                jsonUser2Adm.setStateStr("已申诉");
                jsonUser2Adm.setState(2);
            } else if (state.equals(1)||state.equals(0)){
                jsonUser2Adm.setStateStr("账号正常");
                jsonUser2Adm.setState(state);
            }else {
                jsonUser2Adm.setState(4);
                jsonUser2Adm.setStateStr("已驳回");
            }
            jsonUser2Adm.setId(userInfos.get(i).getId());
            jsonUser2Adm.setRole(0);

            GlobalFunction globalFunction = new GlobalFunction();
            String size = globalFunction.getDirectorySize("/" + username);
            long Lsize = Long.parseLong(size);
            String Ssize = globalFunction.getFileSize(Lsize);
            jsonUser2Adm.setUsedsize(Ssize);
            jsonUser2Adm.setTotalsize("2 GB");
            Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
            BigDecimal bb = new BigDecimal(usedsize*100);
            float f1 = bb.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
            jsonUser2Adm.setUsedInfo(f1);

            jsonUser2Adm.setUsername(user.getName());
            jsonUser2Adms.add(jsonUser2Adm);
        }
        return jsonUser2Adms;
    }

    /*相看指定的人信息*/
    @Override
    public JsonUser2Adm specificDisply(String email) throws URISyntaxException {
        JsonUser2Adm jsonUser2Adm = new JsonUser2Adm();
        List<User> member = userInforDao.query(email);
        User userInfo = member.get(0);

        jsonUser2Adm.setId(userInfo.getId());
        jsonUser2Adm.setUsername(userInfo.getName());
        Integer state = userInfo.getState();
        String username = email.substring(0,email.indexOf("@"));

        if (state.equals(3)) {
            jsonUser2Adm.setStateStr("未申诉 ");
            jsonUser2Adm.setState(3);
        } else if (state.equals(2)){
            jsonUser2Adm.setStateStr("已申诉");
            jsonUser2Adm.setState(2);
        } else if (state.equals(1)||state.equals(0)){
            jsonUser2Adm.setStateStr("账号正常");
            jsonUser2Adm.setState(state);
        }else {
            jsonUser2Adm.setState(4);
            jsonUser2Adm.setStateStr("已驳回");
        }
        GlobalFunction globalFunction = new GlobalFunction();
        try {
            String size = globalFunction.getDirectorySize("/" + username);
            long Lsize = Long.parseLong(size);
            String Ssize = globalFunction.getFileSize(Lsize);
            jsonUser2Adm.setUsedsize(Ssize);
            Integer vip = userInfo.getRole();
            if (vip.equals("2")) {
                jsonUser2Adm.setRole(2);
                jsonUser2Adm.setTotalsize("5 GB");
                Float usedsize = (float) Lsize / (5 * 1000 * 1000 * 1000);
                BigDecimal b = new BigDecimal(usedsize*100);
                float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                jsonUser2Adm.setUsedInfo(f1);
            } else {
                jsonUser2Adm.setRole(1);
                jsonUser2Adm.setTotalsize("2 GB");
                Float usedsize = (float) Lsize / (2 * 1000 * 1000 * 1000);
                BigDecimal b = new BigDecimal(usedsize);
                float f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).floatValue();
                jsonUser2Adm.setUsedInfo(f1);
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return jsonUser2Adm;
    }

    /*成为vip*/
    @Override
    public int tobeVip(String email) {
        return userInforDao.tobeVip(email);
    }

    /*消除vip*/
    @Override
    public int cancleVip(String email) {
        return userInforDao.cancleVip(email);
    }

    /*启用用户*/
    @Override
    @Transactional
    public int modeEnable(String email) {
        complaintRepository.deleteComplaintByUsername(email);
        return userInforDao.modeEnable(email);
    }

    /*冻结用户*/
    @Override
    public int modeFreeze(String email) {
        return userInforDao.modeFreeze(email);
    }

    @Override
    @Transactional
    public String comSub(String email, String cominfo) {
        Complaint complaint1 = new Complaint();
        complaint1.setCominfo(cominfo);
        complaint1.setUsername(email);
        List<Complaint> member = complaintDao.select(email);
        if(member.size()==0){
            complaintDao.insert(complaint1);
        }
        complaintDao.update(complaint1);
        List<User> user = userInforDao.query(email);

        if(user.size()==0){
            return "查无此人！" ;

        }
        User sysUser=user.get(0);
        if (sysUser.getState().equals(1)) {
            return "账号正常使用，不用申诉";
        }
        if (sysUser.getState().equals(2)) {
            return "已经申诉过了，请稍后再试";
        }
        if (sysUser.getState().equals(4)){
            complaintRepository.deleteComplaintByUsername(email);
            return "您之前提交的申诉审核失败,请重新申诉";
        }



        userInforDao.modeEnable1(email);
        System.out.println("申诉成功，请耐心等候");
        return "申诉成功，请耐心等候";
    }


    @Override
    public Complaint comView(String email) {
        Complaint complaint = complaintRepository.findByUsername(email);
        return complaint;
    }

}
