package com.example.yunsystem.dao;

import com.example.yunsystem.entry.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserStoreDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /*增加用户*/
    public int insert(final UserStore user) {
        String sql = "insert into userstore (username,dir,available_capacity,used_capacity) value (?,?,?,?)";
        Object args[] = {user.getUsername(),user.getDir(),user.getAvailableCapacity(),user.getUsedCapacity()};
        return  jdbcTemplate.update(sql, args);
    }
    /*查找用户*/
    public List<UserStore> findByName(String username){
        String sql= "select * from userstore where username=?";
        return   jdbcTemplate.query(sql,new Object[]{username},new BeanPropertyRowMapper(UserStore.class));
    }
    /*更新用户信息*/
    public int update(final UserStore user) {
        String sql = "update userstore set username=?,dir=?,available_capacity=?,used_capacity=? where id=?";
        Object args[] = {user.getUsername(),user.getDir(),user.getAvailableCapacity(),user.getUsedCapacity(), user.getId()};
        return  jdbcTemplate.update(sql, args);
    }

}
