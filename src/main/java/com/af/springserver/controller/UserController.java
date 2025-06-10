package com.af.springserver.controller;

import com.af.springserver.dto.UserDto;
import com.af.springserver.mapper.UserMapper;
import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private User getAuthenticatedUserOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("User is not authenticated");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String email = authentication.getName();
        logger.info("Authenticated user email: {}", email);
        return userService.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @PutMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        User userToSave = userMapper.toEntity(userDto);
        userService.addUser(userToSave);
        return ResponseEntity.ok(userMapper.toDto(userToSave));
    }

    @PostMapping("/get")
    public ResponseEntity<UserDto> getUser(@RequestBody String email) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        return optionalUser
                .map(user -> ResponseEntity.ok(userMapper.toDto(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/patch")
    public ResponseEntity<UserDto> patchUser(@RequestBody UserDto userDto) {
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
                    return ResponseEntity.ok(userMapper.toDto(user));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeUser(@RequestBody Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/add_caregiver")
    public ResponseEntity<UserDto> addUserCaregiver(@RequestBody String data) {
        Optional<User> optionalUser = data.contains("@") ?
                userService.findUserByEmail(data) : userService.findUserByPhoneNumber(data);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User invitedUser = optionalUser.get();

        User loggedUser = getAuthenticatedUserOrThrow();

        if ("caregiver".equals(invitedUser.getRole())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already has caregiver role");
        }

        loggedUser.addCaregiver(invitedUser);
        userService.addUser(loggedUser);

        return ResponseEntity.ok(userMapper.toDto(loggedUser));
    }

    @PostMapping("/remove_caregiver")
    public ResponseEntity<UserDto> removeUserCaregiver(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optionalUser = userService.findUserByEmail(userDto.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User removedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();

        loggedUser.removeCaregiver(removedUser);
        userService.addUser(loggedUser);

        return ResponseEntity.ok(userMapper.toDto(loggedUser));
    }

    @PostMapping("/add_elderly")
    public ResponseEntity<UserDto> addUserElderly(@RequestBody String data) {
        Optional<User> optionalUser = data.contains("@") ?
                userService.findUserByEmail(data) : userService.findUserByPhoneNumber(data);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User invitedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();

        loggedUser.addElderly(invitedUser);
        userService.addUser(loggedUser);

        return ResponseEntity.ok(userMapper.toDto(loggedUser));
    }

    @PostMapping("/remove_elderly")
    public ResponseEntity<UserDto> removeUserElderly(@RequestBody UserDto userDto) {
        if (userDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optionalUser = userService.findUserByEmail(userDto.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User removedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();

        loggedUser.removeElderly(removedUser);
        userService.addUser(loggedUser);

        return ResponseEntity.ok(userMapper.toDto(loggedUser));
    }

    @GetMapping("/get_elderly")
    public ResponseEntity<Set<UserDto>> getUserElderly() {
        User loggedUser = getAuthenticatedUserOrThrow();

        Set<User> elderlySet = loggedUser.getElderly();

        if (elderlySet == null || elderlySet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<UserDto> dtoSet = elderlySet.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtoSet);
    }

    @GetMapping("/get_caretaker")
    public ResponseEntity<Set<UserDto>> getUserCaretaker() {
        User loggedUser = getAuthenticatedUserOrThrow();

        Set<User> caregiverSet = loggedUser.getCaregiver();

        if (caregiverSet == null || caregiverSet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<UserDto> dtoSet = caregiverSet.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtoSet);
    }
}
