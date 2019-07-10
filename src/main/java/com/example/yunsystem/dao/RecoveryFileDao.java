package com.example.yunsystem.dao;

import com.example.yunsystem.entry.RecoveryFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RecoveryFileDao {


    @Autowired
    JdbcTemplate jdbcTemplate;

    /*查找用户*/
    public List<RecoveryFile> findByUsername(String username){
        String sql = "select * from recovery_file where username=?";
        return jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper(RecoveryFile.class));
    }

    /*查找用户*/
    public List<RecoveryFile> findByRecoveryId(String recoveryId){
        String sql = "select * from recovery_file where recovery_id=?";
        return jdbcTemplate.query(sql, new Object[]{recoveryId}, new BeanPropertyRowMapper(RecoveryFile.class));
    }

    /*查找用户*/
    public List<RecoveryFile> findByPresentPath(String presentPath){
        String sql = "select * from recovery_file where present_path=?";
        return jdbcTemplate.query(sql, new Object[]{presentPath}, new BeanPropertyRowMapper(RecoveryFile.class));
    }

    /*查找用户*/
    public int deleteRecycleFile(long id){
        String sql = "delete from recovery_file where recovery_id = ?";
        Object args[] = {id};
        return jdbcTemplate.update(sql, args);

    }

}
