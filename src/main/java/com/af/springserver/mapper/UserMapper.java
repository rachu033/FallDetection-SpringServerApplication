package com.af.springserver.mapper;

import com.af.springserver.dto.UserDto;
import com.af.springserver.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setLanguage(dto.getLanguage());
        user.setTheme(dto.getTheme());
        user.setTokenFCM(dto.getTokenFCM());

        return user;
    }

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setLanguage(user.getLanguage());
        dto.setTheme(user.getTheme());
        dto.setTokenFCM(user.getTokenFCM());

        return dto;
    }
}
