package com.example.yunsystem.dao;

import com.example.yunsystem.entry.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserInforDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /*增加用户*/
    public int insert(final User user) {
        String sql = "insert into user (email,role,logo,code) value (?,?,?,?)";
        Object args[] = {user.getEmail(), user.getRole(), user.getLogo(), user.getCode()};
        return jdbcTemplate.update(sql, args);
    }

    /*查找用户*/
    public List<User> query(String email) {
        String sql = "select * from user where email=?";
        return jdbcTemplate.query(sql, new Object[]{email}, new BeanPropertyRowMapper(User.class));
    }

    /*更新用户信息*/
    public int update(final User user) {
        String sql = "update user set name=?,email=?,password=?,code=? where id=?";
        Object args[] = {user.getName(), user.getEmail(), user.getPassword(), user.getCode(), user.getId()};
        return jdbcTemplate.update(sql, args);
    }

    /*删除邮箱验证失败的数据 */
    public int detele(final User user) {
        String sql = "delete from user where id = ?";
        Object args[] = {user.getId()};
        return jdbcTemplate.update(sql, args);
    }

    /*更新全部用户信息*/
    public int updateAll(final User user) {
        String sql = "update user set name=?,email=?,password=?,role=?,logo=?,code=?,state=? where id=?";
        Object args[] = {user.getName(), user.getEmail(), user.getPassword(), user.getRole(), user.getLogo(), user.getCode(), user.getState(), user.getId()};
        return jdbcTemplate.update(sql, args);
    }

    /*查找用户*/
    public List<User> find(Integer id) {
        String sql = "select * from user where email=?";
        return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper(User.class));
    }

    /*查找管理员*/
    public List<User> findByRoleId() {
        String sql = "select * from user ";
        return jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper(User.class));
    }

    /*查找全部*/
    public List<User> findAll() {
        String sql = "select * from user  where code=? ";
        return jdbcTemplate.query(sql, new Object[]{0}, new BeanPropertyRowMapper(User.class));
    }

    /*查找全部vip*/
    public List<User> findAllVip() {
        String sql = "select * from user where role=? ";
        return jdbcTemplate.query(sql, new Object[]{2}, new BeanPropertyRowMapper(User.class));
    }

    /*查找全部非vip*/
    public List<User> findAllNVip() {
        String sql = "select * from user where role=? ";
        return jdbcTemplate.query(sql, new Object[]{1}, new BeanPropertyRowMapper(User.class));
    }

    /*成为vip*/
    public int tobeVip(String email) {
        String sql = "update user set  role=? where email =? ";
        Object args[] = {2,email};
        return jdbcTemplate.update(sql, args);
    }

    /*成为vip*/
    public int cancleVip(String email) {
        String sql = "update user set  role=? where email =? ";
        Object args[] = {1,email};
        return jdbcTemplate.update(sql, args);
    }

    /*启用用户*/
    public int modeEnable(String email) {
        String sql = "update user set  state=? where email =? ";
        Object args[] = {1,email};
        return jdbcTemplate.update(sql, args);
    }

    /*启用用户*/
    public int modeEnable1(String email) {
        String sql = "update user set  state=? where email =? ";
        Object args[] = {2,email};
        return jdbcTemplate.update(sql, args);
    }

    /*冻结用户*/
    public int modeFreeze(String email) {
        String sql = "update user set  state=? where email =? ";
        Object args[] = {3,email};
        return jdbcTemplate.update(sql, args);
    }

    /*查找用户*/
    public List<User> findbyID(Integer id) {
        String sql = "select * from user where id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper(User.class));
    }


}