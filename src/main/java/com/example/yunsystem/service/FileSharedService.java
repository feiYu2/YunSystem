package com.example.yunsystem.service;
import com.example.yunsystem.util.JsonShare;
import com.example.yunsystem.entry.ShareDetails;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface FileSharedService {
    Map<String, Object> CreateSharedLink(String[] paths, String ifPasswd,String email) throws URISyntaxException, IOException;

    Map<String, Object> ShareVerify(String id, String username);

    List<ShareDetails> AllShare(String email);

    JsonShare ToShare(String id, String passwd) throws IOException;

    String RemoveShare(String[] id);

    String Report(String id);

    String IfSqlById(String id);
}
