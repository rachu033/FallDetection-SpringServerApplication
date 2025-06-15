package com.af.springserver.controller;

import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.af.springserver.security.JwtUtil;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthenticationController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody TokenRequest request) {
        try {
            String idToken = request.getIdToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            String email = decodedToken.getEmail();
            Optional<User> user = userService.findUserByEmail(email);

            String jwt = jwtUtil.generateToken(email);

            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(new TokenResponse(jwt));
            }

            return ResponseEntity.ok().body(new TokenResponse(jwt));
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(401).body("Invalid ID token: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public static class TokenRequest {
        private String idToken;

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    @SuppressWarnings("unused")
    public static class TokenResponse {
        private String jwt;

        public TokenResponse(String jwt) {
            this.jwt = jwt;
        }

        public String getJwt() {
            return jwt;
        }

        public void setJwt(String jwt) {
            this.jwt = jwt;
        }
    }
}
