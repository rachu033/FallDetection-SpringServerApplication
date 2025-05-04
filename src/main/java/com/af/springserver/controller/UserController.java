package com.af.springserver.controller;

import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin // pozwala na zapytania z front-endu np. mobilnego
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Rejestracja nowego użytkownika
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        // (opcjonalnie: sprawdzenie, czy nazwa użytkownika jest już zajęta)
        return ResponseEntity.ok(userService.saveUser(user));
    }

    // Pobieranie użytkownika po nazwie użytkownika
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
