package com.leandro.apiexample.api;

import com.leandro.apiexample.domain.User;
import com.leandro.apiexample.dto.UserDTO;
import com.leandro.apiexample.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserService service;
    private final ModelMapper mapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Integer id) {
        User user = service.findById(id);
        UserDTO userDTO = mapper.map(user, UserDTO.class);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> listDto = service.findAll().stream()
                .map(e -> mapper.map(e, UserDTO.class))
                .toList();
        return ResponseEntity.ok().body(listDto);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        User user = service.save(mapper.map(userDTO, User.class));
        UriComponents uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId());
        return ResponseEntity.created(uri.toUri()).body(mapper.map(user, UserDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Integer id, @RequestBody UserDTO userDTO) {
        User user = mapper.map(userDTO, User.class);
        user.setId(id);
        return ResponseEntity.ok().body(mapper.map(service.update(user), UserDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
