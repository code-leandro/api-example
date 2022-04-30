package com.leandro.apiexample.config;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
@RequiredArgsConstructor
public class LocalConfig {

    private final UserRepository repository;

    @Bean
    public void startDB() {
        User u1 = User
                .builder()
                .name("Leandro")
                .email("leandro.dev@icloud.com")
                .password("101010")
                .build();

        User u2 = User
                .builder()
                .name("Luis")
                .email("luis@gmail.com")
                .password("101010")
                .build();

        repository.saveAll(List.of(u1, u2));
    }
}
