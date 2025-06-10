package com.af.springserver.controller;

import com.af.springserver.dto.UserDto;
import com.af.springserver.mapper.UserMapper;
import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
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

    @PutMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDto userDto) {
        User savedUser = userMapper.toEntity(userDto);
        userService.addUser(savedUser);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/get")
    public ResponseEntity<User> getUser(@RequestBody String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        return optionalUser
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/patch")
    public ResponseEntity<User> patchUser(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return userService.findUserById(userDto.getId())
                .map(user -> {
                    if (userDto.getName() != null) user.setName(userDto.getName());
                    if (userDto.getSurname() != null) user.setSurname(userDto.getSurname());
                    if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
                    if (userDto.getRole() != null) user.setRole(userDto.getRole());
                    if (userDto.getPhoneNumber() != null) user.setPhoneNumber(userDto.getPhoneNumber());
                    if (userDto.getLanguage() != null) user.setLanguage(userDto.getLanguage());
                    if (userDto.getTheme() != null) user.setTheme(userDto.getTheme());

                    userService.addUser(user);
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUser(@RequestBody Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/add_caregiver")
    public ResponseEntity<User> addUserCaregiver(@RequestBody String data) {
        Optional<User> optionalUser;
        if(data.contains("@")) {
            optionalUser = userService.findUserByEmail(data);
        } else {
            optionalUser = userService.findUserByPhoneNumber(data);
        }

        if(optionalUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        User invitedUser = optionalUser.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        Optional<User> loggedInUserOpt = userService.findUserByEmail(email);
        if (loggedInUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User loggedUser = loggedInUserOpt.get();

        if (Objects.equals(invitedUser.getRole(), "caregiver")) {
            return ResponseEntity.noContent().build();
            //TODO: Correct error
        }

        loggedUser.addCaregiver(invitedUser);

        return optionalUser
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/remove_caregiver")
    public ResponseEntity<User> removeUserCaregiver(@RequestBody UserDto userDto) {
        if(userDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optionalUser = userService.findUserByEmail(userDto.getEmail());

        if(optionalUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        User removedUser = optionalUser.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        Optional<User> loggedInUserOpt = userService.findUserByEmail(email);
        if (loggedInUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User loggedUser = loggedInUserOpt.get();

        loggedUser.removeCaregiver(removedUser);

        return optionalUser
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add_elderly")
    public ResponseEntity<User> addUserElderly(@RequestBody String data) {
        Optional<User> optionalUser;
        if(data.contains("@")) {
            optionalUser = userService.findUserByEmail(data);
        } else {
            optionalUser = userService.findUserByPhoneNumber(data);
        }

        if(optionalUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        User invitedUser = optionalUser.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        Optional<User> loggedInUserOpt = userService.findUserByEmail(email);
        if (loggedInUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User loggedUser = loggedInUserOpt.get();

        loggedUser.addElderly(invitedUser);

        return optionalUser
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/remove_elderly")
    public ResponseEntity<User> removeUserElderly(@RequestBody UserDto userDto) {
        if(userDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optionalUser = userService.findUserByEmail(userDto.getEmail());

        if(optionalUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        User removedUser = optionalUser.get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();

        Optional<User> loggedInUserOpt = userService.findUserByEmail(email);
        if (loggedInUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User loggedUser = loggedInUserOpt.get();

        loggedUser.removeElderly(removedUser);

        return optionalUser
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
