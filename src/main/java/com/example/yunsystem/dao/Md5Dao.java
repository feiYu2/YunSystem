package com.example.yunsystem.dao;

import com.example.yunsystem.entry.Md5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Md5Dao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    /*增加用户*/
    public int insert(final Md5 md5) {
        String sql = "insert into md5 (uid,username,path,file_name,file_md5,create_time,update_time) value (?,?,?,?,?,?,?)";
        Object args[] = {md5.getUid(),md5.getUsername(),md5.getPath(),md5.getFileName(),md5.getFileMd5(),md5.getCreateTime(),md5.getUpdateTime()};
        return  jdbcTemplate.update(sql, args);
    }

    /*修改文件名*/
    public int updateFileName(final Md5 u) {
        String sql = "update md5 set path=?,file_name=?,update_time=? where id=?";
        Object args[] = {u.getPath(),u.getFileName(),u.getUpdateTime(),u.getId()};
        return  jdbcTemplate.update(sql, args);
    }

    /*根据文件地址查找文件信息*/
    public List<Md5> query(String path,String filename){
        String sql= "select * from Md5 where path=? and file_name=?";
        return   jdbcTemplate.query(sql,new Object[]{path,filename},new BeanPropertyRowMapper(Md5.class));
    }

    /*查找同一文件夹下的所有文件*/
    public List<Md5> byPathLike(String path){
        String sql= "select * from Md5 where path=? ";
        return   jdbcTemplate.query(sql,new Object[]{path},new BeanPropertyRowMapper(Md5.class));
    }

    public List<Md5> findByFileMd5(String fileMd5){
        String sql= "select * from Md5 where file_md5=? ";
        return   jdbcTemplate.query(sql,new Object[]{fileMd5},new BeanPropertyRowMapper(Md5.class));
    }

    public List<Md5> findByFileNameAndPath(String FileName, String Path){
        String sql= "select * from Md5 where file_name=? and path=?";
        return   jdbcTemplate.query(sql,new Object[]{FileName,Path},new BeanPropertyRowMapper(Md5.class));
    }

    public List<Md5> findByMd5AndPath(String FileMd5, String Path){
        String sql= "select * from Md5 where file_md5=? and path=?";
        return   jdbcTemplate.query(sql,new Object[]{FileMd5,Path},new BeanPropertyRowMapper(Md5.class));
    }

    public List<Md5> findByMd5AndPathAndFilename(String FileMd5,String Path,String FileName){
        String sql= "select * from Md5 where file_md5=? and path=? and file_name=?";
        return   jdbcTemplate.query(sql,new Object[]{FileMd5,Path,FileMd5},new BeanPropertyRowMapper(Md5.class));
    }

    public List<Md5> findByMd5AndFilename(String FileMd5, String FileName){
        String sql= "select * from Md5 where file_md5=? and  file_name=?";
        return   jdbcTemplate.query(sql,new Object[]{FileMd5,FileMd5},new BeanPropertyRowMapper(Md5.class));
    }

    /*删除文件数据 */
    public int detele(Integer id) {
        String sql = "delete from Md5 where id = ?";
        Object args[] = {id};
        return jdbcTemplate.update(sql, args);
    }
}
