package com.example.yunsystem.dao.repository;


import com.example.yunsystem.entry.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface ComplaintRepository extends JpaRepository<Complaint,Integer> {

    Complaint findByUsername(String username);

    void deleteComplaintByUsername(String username);

    @Query("select count(s) from Complaint s")
    Integer countComplaint();

}
