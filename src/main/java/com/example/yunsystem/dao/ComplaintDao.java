package com.example.yunsystem.dao;

import com.example.yunsystem.entry.Complaint;
import com.example.yunsystem.entry.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ComplaintDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*增加用户*/
    public int insert(final Complaint user) {
        String sql = "insert into complaint (username,cominfo) value (?,?)";
        Object args[] = {user.getUsername(), user.getCominfo()};
        return jdbcTemplate.update(sql, args);
    }

    /*增加用户*/
    public List select(String email) {
        String sql= "select * from complaint where username=?";
        return   jdbcTemplate.query(sql,new Object[]{email},new BeanPropertyRowMapper(Complaint.class));
    }

    /*更新用户信息*/
    public int update(final Complaint user) {
        String sql = "update complaint set cominfo=? where username=?";
        Object args[] = {user.getCominfo(),user.getUsername()};
        return jdbcTemplate.update(sql, args);
    }
}
