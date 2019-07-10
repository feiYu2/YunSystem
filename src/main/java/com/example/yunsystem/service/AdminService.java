package com.example.yunsystem.service;
import com.example.yunsystem.entry.Complaint;
import com.example.yunsystem.util.JsonUser2Adm;

import com.example.yunsystem.entry.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface AdminService {
    List<JsonUser2Adm> InfoDisplay() throws URISyntaxException;
    Page<User> UserDisplay(Pageable pageable);
    User userInfoDisplay(String email);
    List<JsonUser2Adm> vipInfoDisPlay()throws IOException,URISyntaxException;
    List<JsonUser2Adm> novipInfoDisPlay() throws IOException,URISyntaxException;
    JsonUser2Adm specificDisply(String email) throws URISyntaxException;
    int tobeVip(String email);
    int cancleVip(String email);
    int modeEnable(String email);
    int modeFreeze(String email);
    String comSub(String email, String cominfo);
    Complaint comView(String email);

}
