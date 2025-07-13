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
        if (query != null && !query.isBlank()) {
            return ResponseEntity.ok(cityService.findCitiesByQuery(query));
        }

        if (city != null && !city.isBlank()) {
            List<String> results;
            if (state != null && !state.isBlank()) {
                results = cityService.findCitiesByCityAndState(city, state);
            } else {
                results = cityService.findCitiesByCity(city);
            }
            return ResponseEntity.ok(results);
        }

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

        return ResponseEntity.ok(cityService.getInfoByCityName(cityName, stateName));
    }
}
