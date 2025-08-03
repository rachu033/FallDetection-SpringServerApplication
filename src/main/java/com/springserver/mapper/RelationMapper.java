package com.springserver.mapper;

import com.springserver.dto.RelationDto;
import com.springserver.model.User;
import org.springframework.stereotype.Component;

@Component
public class RelationMapper {

    @SuppressWarnings("unused")
    public User toEntity(RelationDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPhoneNumber(dto.getPhoneNumber());

        return user;
    }

    public RelationDto toDto(User user) {
        if (user == null) return null;

        RelationDto dto = new RelationDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPhoneNumber(user.getPhoneNumber());

        return dto;
    }
}