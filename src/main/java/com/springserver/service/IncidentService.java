package com.springserver.service;

import com.springserver.model.Incident;
import com.springserver.model.User;
import com.springserver.repository.IncidentRepository;
import com.springserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    public Incident addIncident(Incident incident, User user) {
        incident.setUser(user);

        Incident savedIncident = incidentRepository.save(incident);

        user.addReport(savedIncident);

        for (User caregiver : user.getCaregiver()) {
            caregiver.addIncident(savedIncident);
            userRepository.save(caregiver);
        }

        userRepository.save(user);

        return savedIncident;
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    public List<Incident> findIncomeIncidentsByUserId(Long userId) {
        return incidentRepository.findIncomeIncidentsByUserId(userId);
    }

    public List<Incident> findOutcomeIncidentsByUserId(Long userId) {
        return incidentRepository.findOutcomeIncidentsByUserId(userId);
    }

    public List<Incident> findAllIncidentsByUserId(Long userId) {
        return incidentRepository.findAllIncidentsByUserId(userId);
    }
}