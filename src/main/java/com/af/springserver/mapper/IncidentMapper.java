package com.af.springserver.mapper;

import com.af.springserver.dto.IncidentDto;
import com.af.springserver.model.Incident;
import com.af.springserver.model.User;
import com.af.springserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IncidentMapper {
    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        IncidentMapper.userService = userService;
    }

    public static IncidentDto toDto(Incident incident) {
        if (incident == null) {
            return null;
        }

        IncidentDto dto = new IncidentDto();
        dto.setId(incident.getId());
        dto.setUserId(incident.getUser().getId());
        dto.setTimestamp(incident.getTimestamp());
        dto.setType(incident.getType());
        dto.setLatitude(incident.getLatitude());
        dto.setLongitude(incident.getLongitude());
        dto.setAcknowledged(incident.isAcknowledged());

        return dto;
    }

    public static Incident toEntity(IncidentDto dto) {
        if (dto == null) {
            return null;
        }

        Optional<User> optionalUser = userService.findUserById(dto.getUserId());

        User user = optionalUser.orElse(null);

        Incident incident = new Incident();
        incident.setUser(user);
        incident.setTimestamp(dto.getTimestamp());
        incident.setType(dto.getType());
        incident.setLatitude(dto.getLatitude());
        incident.setLongitude(dto.getLongitude());
        incident.setAcknowledged(dto.isAcknowledged());

        return incident;
    }
}
