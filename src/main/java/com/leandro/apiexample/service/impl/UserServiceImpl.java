package com.leandro.apiexample.service.impl;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.exception.NotFoundException;
import com.leandro.apiexample.repository.UserRepository;
import com.leandro.apiexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Object not found!"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
