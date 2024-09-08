package com.epam.ASEAT.controller;

import com.epam.ASEAT.service.FlightAggregatorService;
import com.epam.ASEAT.service.GenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.epam.ASEAT.data.RO.FlightRouteRO;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightRouteController {
    @Autowired
    private FlightAggregatorService flightAggregatorService;

    @Autowired
    private GenAIService genAIService;


    @GetMapping("/routes")
    public ResponseEntity<List<FlightRouteRO>> getAllRoutes() {
        List<FlightRouteRO> flightRoutes = flightAggregatorService.getAggregatedFlightRoutes();
        return ResponseEntity.ok(flightRoutes);
    }

    @GetMapping("/ask")
    public ResponseEntity<String> askQuestion(@RequestParam String question) {
        List<FlightRouteRO> flightRoutes = flightAggregatorService.getAggregatedFlightRoutes();
        String answer = genAIService.getAnswer(question, flightRoutes);
        return ResponseEntity.ok(answer);
    }
}
