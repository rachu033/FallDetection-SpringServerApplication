package com.af.springserver.controller;

import com.af.springserver.dto.IncidentDto;
import com.af.springserver.mapper.IncidentMapper;
import com.af.springserver.model.Incident;
import com.af.springserver.model.User;
import com.af.springserver.service.IncidentService;
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

    @Autowired
    public IncidentController(IncidentService incidentService, UserService userService) {
        this.incidentService = incidentService;
        this.userService = userService;
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
        System.out.println(incidentDto);
        Incident incident = IncidentMapper.toEntity(incidentDto);
        Incident savedIncident = incidentService.addIncidentForUser(incident, user);
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
