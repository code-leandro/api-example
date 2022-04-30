package com.leandro.apiexample.service;

import com.leandro.apiexample.domain.User;

public interface UserService {

    User findById(Integer id);
}
