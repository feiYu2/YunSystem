package com.example.yunsystem.service.servicelmpl;

import com.example.yunsystem.service.UserStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.yunsystem.entry.UserStore;
import com.example.yunsystem.dao.repository.UserStoreRespository;

@Service
public class UserStoreServicelmpl implements UserStoreService {
    @Autowired
    private UserStoreRespository userStoreRepository;
    @Override
    public UserStore insert(UserStore userStore) {
        return userStoreRepository.save(userStore);
    }

    @Override
    public UserStore update(UserStore userStore) {
        return userStoreRepository.save(userStore);
    }

    @Override
    public UserStore findByUsername(String username) {
        return userStoreRepository.findByUsername(username);
    }
}
