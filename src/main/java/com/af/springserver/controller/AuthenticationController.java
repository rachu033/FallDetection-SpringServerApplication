package com.af.springserver.controller;

import com.af.springserver.mapper.UserMapper;
import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AuthenticationController(JwtUtil jwtUtil, UserService userService, UserMapper userMapper) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody TokenRequest request) {
        try {
            String idToken = request.getIdToken();
            System.out.println(idToken);
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            String email = decodedToken.getEmail();
            Optional<User> user = userService.getUser(email);

            if (user.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            String uid = decodedToken.getUid();

            String jwt = jwtUtil.generateToken(uid);

            System.out.println(jwt);

            return ResponseEntity.ok().body(new TokenResponse(jwt));
        } catch (FirebaseAuthException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(401).body("Invalid ID token: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Internal error: " + e.getMessage());
        }
    }

    public static class TokenRequest {
        private String idToken;

        public String getIdToken() {
            return idToken;
        }

        public void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

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

    private String generateCustomJwt(String uid, String email) {
        return "mocked-jwt-for-" + uid;
    }
}
