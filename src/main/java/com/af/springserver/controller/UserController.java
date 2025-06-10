package com.af.springserver.controller;

import com.af.springserver.dto.UserDto;
import com.af.springserver.mapper.UserMapper;
import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        System.out.println("Rejestruje");
        User savedUser = userMapper.toEntity(userDto);
        userService.addUser(savedUser);
        return ResponseEntity.ok(savedUser);
    }

//    @GetMapping("/get")
//    public ResponseEntity<User> getUser(@RequestParam Long id) {
//        Optional<User> optionalUser = userService.getUser(id);
//        return optionalUser
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestParam String email) {
        Optional<User> optionalUser = userService.getUser(email);
        return optionalUser
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }




}
