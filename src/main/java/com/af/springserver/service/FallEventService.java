package com.af.springserver.service;

import com.af.springserver.model.Incident;
import com.af.springserver.repository.FallEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FallEventService {

    @Autowired
    private FallEventRepository fallEventRepository;

    public Incident saveFall(Incident incident) {
        incident.setTimestamp(new Date());
        return fallEventRepository.save(incident);
    }

    public List<Incident> getFallsByUserId(Long userId) {
        return fallEventRepository.findByUserId(userId);
    }
}
