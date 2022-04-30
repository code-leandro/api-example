package com.leandro.apiexample.service;

import com.leandro.apiexample.domain.User;

import java.util.List;

public interface UserService {

    User findById(Integer id);
    List<User> findAll();
    User save(User user);
}
