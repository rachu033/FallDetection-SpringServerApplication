package com.af.springserver.controller;

import com.af.springserver.dto.RelationDto;
import com.af.springserver.dto.UserDto;
import com.af.springserver.mapper.RelationMapper;
import com.af.springserver.mapper.UserMapper;
import com.af.springserver.model.User;
import com.af.springserver.service.NotificationService;
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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final RelationMapper relationMapper;
    private final NotificationService notificationService;


    @Autowired
    public UserController(UserService userService, UserMapper userMapper, RelationMapper relationMapper, NotificationService notificationService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.relationMapper = relationMapper;
        this.notificationService = new NotificationService();
    }

    private User getAuthenticatedUserOrThrow() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
//        }
//        String email = authentication.getName();
        String email = "rachubaadam2003@gmail.com";
        return userService.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @PutMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        User userToSave = userMapper.toEntity(userDto);
        userService.addUser(userToSave);
        return ResponseEntity.ok(userMapper.toDto(userToSave));
    }

    @GetMapping("/get")
    public ResponseEntity<UserDto> getUser() {
        User user = getAuthenticatedUserOrThrow();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PatchMapping("/patch")
    public ResponseEntity<UserDto> patchUser(@RequestBody UserDto userDto) {
        System.out.println("Aktualizacja: " + userDto);
        if (userDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        User loggedUser = getAuthenticatedUserOrThrow();
        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!userDto.getId().equals(loggedUser.getId()) || !userDto.getEmail().equals(loggedUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        notificationService.sendPushNotification(loggedUser.getTokenFCM(), "Chuj", "Cipia");

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
    public ResponseEntity<RelationDto> addUserCaregiver(@RequestBody String data) {
        data = data.replace("\"", "");
        Optional<User> optionalUser = data.contains("@") ?
                userService.findUserByEmail(data) : userService.findUserByPhoneNumber(data);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User invitedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();

        // Dodajemy po stronie właściciela
        invitedUser.addElderly(loggedUser);
        userService.addUser(invitedUser);

        System.out.println("Dodano: " + data);
        return ResponseEntity.ok(relationMapper.toDto(invitedUser));
    }

    @PostMapping("/remove_caregiver")
    public ResponseEntity<Map<String, String>> removeUserCaregiver(@RequestBody RelationDto relationDto) {
        System.out.println("usuwanie: " + relationDto);
        if (relationDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optionalUser = userService.findUserByEmail(relationDto.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User removedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();

        removedUser.removeElderly(loggedUser);
        userService.addUser(removedUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successful");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/add_elderly")
    public ResponseEntity<RelationDto> addUserElderly(@RequestBody String data) {
        data = data.replace("\"", "");
        Optional<User> optionalUser = data.contains("@") ?
                userService.findUserByEmail(data) : userService.findUserByPhoneNumber(data);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User invitedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();
        loggedUser.addElderly(invitedUser);
        userService.addUser(loggedUser);

        return ResponseEntity.ok(relationMapper.toDto(invitedUser));
    }

    @PostMapping("/remove_elderly")
    public ResponseEntity<Map<String, String>> removeUserElderly(@RequestBody RelationDto relationDto) {
        if (relationDto.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<User> optionalUser = userService.findUserByEmail(relationDto.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User removedUser = optionalUser.get();
        User loggedUser = getAuthenticatedUserOrThrow();

        loggedUser.removeElderly(removedUser);
        userService.addUser(loggedUser);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Successful");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_elderly")
    public ResponseEntity<Set<RelationDto>> getUserElderly() {
        User loggedUser = getAuthenticatedUserOrThrow();

        Set<User> elderlySet = loggedUser.getElderly();

        if (elderlySet == null || elderlySet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<RelationDto> dtoSet = elderlySet.stream()
                .map(relationMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtoSet);
    }

    @GetMapping("/get_caregiver")
    public ResponseEntity<Set<RelationDto>> getUserCaretaker() {
        User loggedUser = getAuthenticatedUserOrThrow();

        Set<User> caregiverSet = loggedUser.getCaregiver();

        if (caregiverSet == null || caregiverSet.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Set<RelationDto> dtoSet = caregiverSet.stream()
                .map(relationMapper::toDto)
                .collect(Collectors.toSet());

        return ResponseEntity.ok(dtoSet);
    }
}