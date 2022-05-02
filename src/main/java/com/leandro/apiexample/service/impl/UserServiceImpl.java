package com.leandro.apiexample.service.impl;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.exception.DataIntegrityViolationException;
import com.leandro.apiexample.exception.NotFoundException;
import com.leandro.apiexample.repository.UserRepository;
import com.leandro.apiexample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String OBJECT_NOT_FOUND = "Object not found!";
    public static final String EMAIL_ALREADY_EXIST = "Email already exist!";
    public static final String EMAIL_ALREADY_IN_OTHER_USER = "Email already in other user!";
    private final UserRepository userRepository;

    @Override
    public User findById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new NotFoundException(OBJECT_NOT_FOUND));
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
        validationEmailUpdate(user.getId(), user.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        userRepository.deleteById(id);
    }

    public void validationEmailExist(@NotNull User user) {
        List<User> listUsers = userRepository.findByEmail(user.getEmail());
        if (! listUsers.isEmpty())
            throw new DataIntegrityViolationException(EMAIL_ALREADY_EXIST);
    }

    public void validationEmailUpdate(Integer idUpdate, String email) {
        List<User> listUsers = userRepository.findByEmail(email);
        List<User> otherUserWithEmail = listUsers.stream().filter(e -> !e.getId().equals(idUpdate)).toList();

        if (! otherUserWithEmail.isEmpty())
            throw new DataIntegrityViolationException(EMAIL_ALREADY_IN_OTHER_USER);
    }
}
