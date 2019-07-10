package com.example.yunsystem.dao.repository;



import com.example.yunsystem.entry.ShareDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ShareDetailsRepository extends JpaRepository<ShareDetails, Integer> {

    List<ShareDetails> findAllByUsername(String username);

    ShareDetails findByCharId(String charId);

    List<ShareDetails> findAll();

    @Modifying
    @Transactional
    @Query("delete from ShareDetails sd where sd.charId=?1")
    void deleteByCharId(String charId);

    List<ShareDetails> findByUsername(String username);


}
