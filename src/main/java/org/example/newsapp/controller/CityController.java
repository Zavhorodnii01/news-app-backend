package org.example.newsapp.controller;

import org.example.newsapp.controller.dto.CityDto;
import org.example.newsapp.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping
    public ResponseEntity<List<String>> getCities(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state
    ) {
        // Handles requests to retrieve a list of cities based on a query, specific city, or state
        if (query != null && !query.isBlank()) {
            return ResponseEntity.ok(cityService.findCitiesByQuery(query));
        }

        if (city != null && !city.isBlank()) {
            // Retrieves city information based on city name and optionally state name
            List<String> results;
            if (state != null && !state.isBlank()) {
                results = cityService.findCitiesByCityAndState(city, state);
            } else {
                results = cityService.findCitiesByCity(city);
            }
            return ResponseEntity.ok(results);
        }

        // Returns the 100 most populated cities with their state names
        return ResponseEntity.ok(cityService.get100MostPopulatedCities()
                .stream()
                .map(c -> c.getCity() + ", " + c.getStateName())
                .distinct()
                .limit(100)
                .toList());
    }


    @GetMapping("/{cityName}/{stateName}")
    public ResponseEntity<CityDto> getInfoByCity(
            @PathVariable String cityName,
            @PathVariable String stateName
    ) {
        // Retrieves detailed information about a specific city in a specific state
        return ResponseEntity.ok(cityService.getInfoByCityName(cityName, stateName));
    }
}
