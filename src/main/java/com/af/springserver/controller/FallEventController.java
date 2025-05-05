package com.af.springserver.controller;

import com.af.springserver.model.FallEvent;
import com.af.springserver.service.FallEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/falls")
@CrossOrigin
public class FallEventController {

    @Autowired
    private FallEventService fallEventService;

    @PostMapping
    public ResponseEntity<FallEvent> reportFall(@RequestBody FallEvent fallEvent) {
        return ResponseEntity.ok(fallEventService.saveFall(fallEvent));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FallEvent>> getFallsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(fallEventService.getFallsByUserId(userId));
    }
}
