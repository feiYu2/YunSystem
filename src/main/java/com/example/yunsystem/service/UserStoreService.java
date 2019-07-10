package com.example.yunsystem.service;

import com.example.yunsystem.entry.UserStore;

public interface UserStoreService {
    UserStore insert (UserStore userStore);

    UserStore update(UserStore userStore);

    UserStore findByUsername(String username);
}