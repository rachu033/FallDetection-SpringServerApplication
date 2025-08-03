package com.springserver.dto;

@SuppressWarnings("unused")
public class UserDto {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String phoneNumber;
    private String language;
    private String theme;
    private String tokenFCM;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }

    public String getTokenFCM() {
        return tokenFCM;
    }
    public void setTokenFCM(String tokenFCM) {
        this.tokenFCM = tokenFCM;
    }
}
