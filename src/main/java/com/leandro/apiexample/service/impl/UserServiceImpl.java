package com.leandro.apiexample.service.impl;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.exception.DataIntegrityViolationException;
import com.leandro.apiexample.exception.NotFoundException;
import com.leandro.apiexample.repository.UserRepository;
import com.leandro.apiexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        validationEmailExist(user);
        return userRepository.save(user);
    }

    public void validationEmailExist(User user) {
        Optional<User> userFound = userRepository.findByEmail(user.getEmail());
        if (userFound.isPresent())
            throw new DataIntegrityViolationException("Email already exist!");
    }
}
