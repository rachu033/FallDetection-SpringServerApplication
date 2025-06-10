package com.af.springserver.controller;

import com.af.springserver.model.Incident;
import com.af.springserver.service.FallEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/incident")
@CrossOrigin
public class IncidentController {

    @Autowired
    private FallEventService fallEventService;

    @PostMapping
    public ResponseEntity<Incident> reportFall(@RequestBody Incident incident) {
        return ResponseEntity.ok(fallEventService.saveFall(incident));
    }

    @GetMapping("/get")
    public ResponseEntity<Map<String, String>> sayHello() {
        System.out.println("Hello");
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, world!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Incident>> getFallsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(fallEventService.getFallsByUserId(userId));
    }
}
