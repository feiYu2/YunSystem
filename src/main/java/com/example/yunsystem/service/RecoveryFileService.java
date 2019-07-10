package com.example.yunsystem.service;


import com.example.yunsystem.entry.RecoveryFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface RecoveryFileService {
    /**
     * 文件移动
     */
    boolean MoveToRecovery(String oriPath, String dstPath) throws IOException;

    /**
     * 回收站文件删除
     */
    boolean deleteFile(String filePath) throws IOException, URISyntaxException;

    /**
     * 文件移入回收站时插入数据库
     */
    RecoveryFile insert(RecoveryFile recoveryFile);

    /**
     * 删除回收站文件
     */
    void deleteRecoveryFile(Long id);

    List<RecoveryFile> findByUsername(String username);

    RecoveryFile findByRecoveryId(Long recoveryId);

    RecoveryFile findByPresentPath(String presentPath);

}
