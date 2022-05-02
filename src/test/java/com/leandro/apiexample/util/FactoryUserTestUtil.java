package com.leandro.apiexample.util;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FactoryUserTestUtil {

    public static final int ID = 1000;
    public static final int ID_2 = 1002;
    public static final int ID_NOT_FOUND = 999;
    public static final String MY_NAME = "MY_NAME";
    public static final String MY_EMAIL = "MY_EMAIL@domain.com";
    public static final String OTHER_EMAIL = "OTHER@domain.com";
    public static final String MY_PASS = "MY-PASS";
    public static final String MY_NEW_PASS = "MY-PASS-NEW";
    static ModelMapper mapper = new ModelMapper();

    public static User user() {
        return User.builder()
                .id(ID)
                .name(MY_NAME)
                .email(MY_EMAIL)
                .password(MY_PASS)
                .build();
    }

    public static UserDTO userDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }

    public static User userOtherEmail() {
        return User.builder()
                .id(ID_2)
                .name(MY_NAME)
                .email(OTHER_EMAIL)
                .password(MY_PASS)
                .build();
    }

    public static User userAfterUpdate() {
        User user = user();
        user.setPassword(MY_NEW_PASS);
        return user;
    }

    public static User userBeforeSave() {
        return User.builder()
                .name(MY_NAME)
                .email(MY_EMAIL)
                .password(MY_PASS)
                .build();
    }

    public static Optional<User> optionalUser() {
        return Optional.of(user());
    }

    public static List<User> listUser() {
        return List.of(user());
    }
}
