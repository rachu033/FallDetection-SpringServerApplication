package com.af.springserver.controller;

import com.af.springserver.dto.IncidentDto;
import com.af.springserver.mapper.IncidentMapper;
import com.af.springserver.model.Incident;
import com.af.springserver.model.User;
import com.af.springserver.service.IncidentService;
import com.af.springserver.service.NotificationService;
import com.af.springserver.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/incident")
@CrossOrigin
public class IncidentController {

    private final IncidentService incidentService;
    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public IncidentController(IncidentService incidentService, UserService userService, NotificationService notificationService) {
        this.incidentService = incidentService;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    private User getAuthenticatedUserOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        String email = authentication.getName();
        return userService.findUserByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }

    @PutMapping("/add")
    public ResponseEntity<IncidentDto> createIncident(@RequestBody IncidentDto incidentDto) {
        User user = getAuthenticatedUserOrThrow();
        Incident incident = IncidentMapper.toEntity(incidentDto);
        Incident savedIncident = incidentService.addIncident(incident, user);
        var caregivers = user.getCaregiver();
        if (caregivers != null) {
            for (User caregiver : caregivers) {
                String tokenFCM = caregiver.getTokenFCM();
                String title = incident.getType();
                String body = incident.getUser().getId() + " " + incident.getLatitude() + " " + incident.getLongitude();
                notificationService.sendDataNotification(tokenFCM, title, body);
            }
        }
        return ResponseEntity.ok(IncidentMapper.toDto(savedIncident));
    }

    @GetMapping("/get_outcome")
    public ResponseEntity<List<IncidentDto>> getOutcomeIncidents() {
        User user = getAuthenticatedUserOrThrow();
        List<Incident> incidents = incidentService.findOutcomeIncidentsByUserId(user.getId());
        List<IncidentDto> dtoList = incidents.stream()
                .map(IncidentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/get_income")
    public ResponseEntity<List<IncidentDto>> getIncomeIncidents() {
        User user = getAuthenticatedUserOrThrow();
        List<Incident> incidents = incidentService.findIncomeIncidentsByUserId(user.getId());
        List<IncidentDto> dtoList = incidents.stream()
                .map(IncidentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<IncidentDto>> getAllIncidents() {
        User user = getAuthenticatedUserOrThrow();
        List<Incident> incidents = incidentService.findAllIncidentsByUserId(user.getId());
        List<IncidentDto> dtoList = incidents.stream()
                .map(IncidentMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}
