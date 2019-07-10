package com.example.yunsystem.util;

public class JsonUser2Adm {
    private Integer id;
    private String username;
    private String email;
    private Integer state;
    private Integer role;
    private String stateStr;
    private String totalsize;
    private String usedsize;
    private Float usedInfo;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getStateStr() {
        return stateStr;
    }

    public void setStateStr(String stateStr) {
        this.stateStr = stateStr;
    }

    public String getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(String totalsize) {
        this.totalsize = totalsize;
    }

    public String getUsedsize() {
        return usedsize;
    }

    public void setUsedsize(String usedsize) {
        this.usedsize = usedsize;
    }

    public Float getUsedInfo() {
        return usedInfo;
    }

    public void setUsedInfo(Float usedInfo) {
        this.usedInfo = usedInfo;
    }


}
