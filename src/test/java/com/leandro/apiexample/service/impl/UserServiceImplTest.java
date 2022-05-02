package com.leandro.apiexample.service.impl;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.exception.DataIntegrityViolationException;
import com.leandro.apiexample.exception.NotFoundException;
import com.leandro.apiexample.repository.UserRepository;
import com.leandro.apiexample.util.FactoryUserTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository repository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("findById an return the instance ok! ")
    void findById() {
        when(repository.findById(FactoryUserTestUtil.ID)).thenReturn(FactoryUserTestUtil.optionalUser());
        User userFound = userService.findById(FactoryUserTestUtil.ID);
        assertEquals(FactoryUserTestUtil.MY_EMAIL, userFound.getEmail());
    }

    @Test
    @DisplayName("findById and thrown an exception not found! ")
    void findByIdNotFound() {
        Mockito.when(repository.findById(FactoryUserTestUtil.ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.findById(FactoryUserTestUtil.ID));
    }

    @Test
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(FactoryUserTestUtil.listUser());
        List<User> list = userService.findAll();
        assertNotNull(list);
        assertEquals(1, list.size());
        User user = list.get(0);
        assertEquals(FactoryUserTestUtil.ID, user.getId());
    }

    @Test
    void save() {
        User userBeforeSave = FactoryUserTestUtil.userBeforeSave();
        when(repository.save(userBeforeSave)).thenReturn(FactoryUserTestUtil.user());
        when(repository.findByEmail(FactoryUserTestUtil.MY_EMAIL)).thenReturn(List.of());

        User userSaved = userService.save(userBeforeSave);
        assertEquals(FactoryUserTestUtil.ID, userSaved.getId());
    }

    @Test
    void saveEmailAlreadyExist() {
        User userBeforeSave = FactoryUserTestUtil.userBeforeSave();
        when(repository.save(userBeforeSave)).thenReturn(FactoryUserTestUtil.user());
        when(repository.findByEmail(FactoryUserTestUtil.MY_EMAIL)).thenReturn(List.of(FactoryUserTestUtil.userOtherEmail()));
        assertThrows(DataIntegrityViolationException.class, () -> userService.save(userBeforeSave));
    }

    @Test
    void update() {
        User userBeforeUpdate = FactoryUserTestUtil.user();
        when(repository.save(userBeforeUpdate)).thenReturn(FactoryUserTestUtil.userAfterUpdate());
        when(repository.findByEmail(FactoryUserTestUtil.MY_EMAIL)).thenReturn(List.of(userBeforeUpdate));
        User userUpdated = userService.update(userBeforeUpdate);
        assertEquals(FactoryUserTestUtil.MY_NEW_PASS, userUpdated.getPassword());
    }

    @Test
    @DisplayName("trying update the user for an email that exist in other user")
    void updateEmailExist() {
        User userBeforeUpdate = FactoryUserTestUtil.user();
        userBeforeUpdate.setEmail(FactoryUserTestUtil.OTHER_EMAIL);
        when(repository.findByEmail(FactoryUserTestUtil.OTHER_EMAIL)).thenReturn(List.of(FactoryUserTestUtil.userOtherEmail()));
        assertThrows(DataIntegrityViolationException.class, () -> userService.update(userBeforeUpdate));
    }

    @Test
    void delete() {
        Optional<User> optionalUser = FactoryUserTestUtil.optionalUser();
        when(repository.findById(FactoryUserTestUtil.ID)).thenReturn(optionalUser);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            doNothing().when(repository).delete(user);
            userService.delete(FactoryUserTestUtil.ID);
            verify(repository, times(1)).deleteById(user.getId());
        }
    }

    @Test
    void deleteNotFound() {
        when(repository.findById(FactoryUserTestUtil.ID_NOT_FOUND)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> userService.delete(FactoryUserTestUtil.ID_NOT_FOUND));
    }

    @Test
    void validationEmailExist() {
        when(repository.findByEmail(FactoryUserTestUtil.MY_EMAIL)).thenReturn(List.of());
        userService.validationEmailExist(FactoryUserTestUtil.userBeforeSave());
        verify(repository, times(1)).findByEmail(FactoryUserTestUtil.MY_EMAIL);
    }

    @Test
    void validationEmailExistThrowException() {
        when(repository.findByEmail(FactoryUserTestUtil.MY_EMAIL)).thenReturn(FactoryUserTestUtil.listUser());
        assertThrows(DataIntegrityViolationException.class, () -> userService.validationEmailExist(FactoryUserTestUtil.userBeforeSave()));
    }

    @Test
    void validationEmailUpdate() {
    }
}