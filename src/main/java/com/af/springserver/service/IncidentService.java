package com.af.springserver.service;

import com.af.springserver.model.Incident;
import com.af.springserver.model.User;
import com.af.springserver.repository.IncidentRepository;
import com.af.springserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    @Autowired
    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    public void addIncident(Incident incident) {
        incidentRepository.save(incident);
    }

    public void deleteIncident(Long id) {
        incidentRepository.deleteById(id);
    }

    public Optional<Incident> findIncidentById(Long id) {
        return incidentRepository.findById(id);
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

    public Incident addIncidentForUser(Incident incident, User user) {
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
}