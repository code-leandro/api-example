package com.leandro.apiexample.api;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.dto.UserDTO;
import com.leandro.apiexample.exception.NotFoundException;
import com.leandro.apiexample.service.UserService;
import com.leandro.apiexample.util.FactoryUserTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService service;

    @Mock
    ModelMapper mapper;

    @Test
    @DisplayName("findById -> response OK")
    void findById() {
        UserDTO userDTO = FactoryUserTestUtil.userDTO(FactoryUserTestUtil.user());

        when(service.findById(FactoryUserTestUtil.ID)).thenReturn(FactoryUserTestUtil.user());
        when(mapper.map(FactoryUserTestUtil.user(), UserDTO.class)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.findById(FactoryUserTestUtil.ID);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FactoryUserTestUtil.MY_EMAIL, response.getBody().getEmail());
    }

    @Test
    @DisplayName("findById -> NotFoundException")
    void findByIdGenerateNotFound() {
        when(service.findById(any())).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> userController.findById(FactoryUserTestUtil.ID));
    }

    @Test
    void findAll() {
        User user = FactoryUserTestUtil.user();
        when(service.findAll()).thenReturn(FactoryUserTestUtil.listUser());
        when(mapper.map(user, UserDTO.class)).thenReturn(FactoryUserTestUtil.userDTO(user));
        ResponseEntity<List<UserDTO>> all = userController.findAll();
        List<UserDTO> list = all.getBody();
        assertThat(list).isNotNull().isNotEmpty().hasSize(1);
        assertThat(list.get(0).getEmail()).isEqualTo(FactoryUserTestUtil.MY_EMAIL);
    }

    @Test
    void save() {
        User userBeforeSave = FactoryUserTestUtil.userBeforeSave();
        UserDTO userDTOBeforeSave = FactoryUserTestUtil.userDTO(userBeforeSave);
        User userAfterSaved = FactoryUserTestUtil.user();

        when(mapper.map(userDTOBeforeSave, User.class)).thenReturn(userBeforeSave);
        when(service.save(userBeforeSave)).thenReturn(userAfterSaved);
        when(mapper.map(userAfterSaved, UserDTO.class)).thenReturn(FactoryUserTestUtil.userDTO(userAfterSaved));

        ResponseEntity<UserDTO> response = userController.save(userDTOBeforeSave);
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isInstanceOf(UserDTO.class).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(FactoryUserTestUtil.MY_EMAIL);
        assertThat(response.getBody().getId()).isEqualTo(FactoryUserTestUtil.ID);
        assertThat(response.getBody().getName()).isEqualTo(FactoryUserTestUtil.MY_NAME);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void update() {
        User user = FactoryUserTestUtil.user();
        UserDTO userDTO = FactoryUserTestUtil.userDTO(user);
        User userAfterUpdate = FactoryUserTestUtil.userAfterUpdate();
        UserDTO userDTOAfterUpdate = FactoryUserTestUtil.userDTO(userAfterUpdate);

        when(mapper.map(userDTO, User.class)).thenReturn(user);
        when(service.update(user)).thenReturn(userAfterUpdate);
        when(mapper.map(userAfterUpdate, UserDTO.class)).thenReturn(userDTOAfterUpdate);

        ResponseEntity<UserDTO> response = userController.update(FactoryUserTestUtil.ID, userDTO);

        UserDTO updated = response.getBody();
        assertThat(response).isNotNull();
        assertThat(updated).isInstanceOf(UserDTO.class).isNotNull();
        assertThat(updated.getPassword()).isEqualTo(FactoryUserTestUtil.MY_NEW_PASS);
    }

    @Test
    void delete() {
        doNothing().when(service).delete(FactoryUserTestUtil.ID);
        ResponseEntity<Void> response = userController.delete(FactoryUserTestUtil.ID);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(service, times(1)).delete(FactoryUserTestUtil.ID);
    }
}