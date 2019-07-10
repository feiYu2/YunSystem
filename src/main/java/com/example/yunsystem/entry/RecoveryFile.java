package com.example.yunsystem.entry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 回收站文件
 */

@Entity
public class RecoveryFile {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long recoveryId;

    private String username;

    /**删除文件的原路径(还原用).*/
    private String originalPath;

    /**文件的现在路径*/
    private String presentPath;

    private String type;

    private String fileName;

    private String size;

    private Long len;

    private String delTime;


    public Long getRecoveryId() {
        return recoveryId;
    }

    public void setRecoveryId(Long recoveryId) {
        this.recoveryId = recoveryId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getPresentPath() {
        return presentPath;
    }

    public void setPresentPath(String presentPath) {
        this.presentPath = presentPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getLen() {
        return len;
    }

    public void setLen(Long len) {
        this.len = len;
    }

    public String getDelTime() {
        return delTime;
    }

    public void setDelTime(String delTime) {
        this.delTime = delTime;
    }
}
