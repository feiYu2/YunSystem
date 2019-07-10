package com.example.yunsystem.dao.repository;

import com.example.yunsystem.entry.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInforRepository  extends JpaRepository<User,Integer> {
    Page<User> findAll (Pageable pageable);
}
