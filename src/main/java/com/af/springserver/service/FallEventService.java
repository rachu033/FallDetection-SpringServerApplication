package com.af.springserver.service;

import com.af.springserver.model.FallEvent;
import com.af.springserver.repository.FallEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FallEventService {

    @Autowired
    private FallEventRepository fallEventRepository;

    public FallEvent saveFall(FallEvent fallEvent) {
        fallEvent.setTimestamp(new Date()); // automatyczne ustawienie czasu
        return fallEventRepository.save(fallEvent);
    }

    public List<FallEvent> getFallsByUserId(Long userId) {
        return fallEventRepository.findByUserId(userId);
    }
}
