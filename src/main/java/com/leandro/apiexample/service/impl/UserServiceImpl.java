package com.leandro.apiexample.service.impl;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.exception.DataIntegrityViolationException;
import com.leandro.apiexample.exception.NotFoundException;
import com.leandro.apiexample.repository.UserRepository;
import com.leandro.apiexample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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

    @Override
    public User update(User user) {
        log.info("Updating {}", user);
        validationEmailUpdate(user.getId(), user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        userRepository.delete(findById(id));
    }

    public void validationEmailExist(User user) {
        List<User> listUsers = userRepository.findByEmail(user.getEmail());
        if (! listUsers.isEmpty())
            throw new DataIntegrityViolationException("Email already exist!");
    }

    public void validationEmailUpdate(Integer idUpdate, String email) {
        List<User> listUsers = userRepository.findByEmail(email);
        List<User> otherUserWithEmail = listUsers.stream().filter(e -> !e.getId().equals(idUpdate)).toList();

        if (! otherUserWithEmail.isEmpty())
            throw new DataIntegrityViolationException("Email already in other user!");
    }
}
