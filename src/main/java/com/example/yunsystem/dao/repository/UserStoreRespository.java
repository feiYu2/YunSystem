package com.example.yunsystem.dao.repository;
import com.example.yunsystem.entry.UserStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStoreRespository extends JpaRepository<UserStore,Integer> {
    UserStore findByUsername(String username);
}
