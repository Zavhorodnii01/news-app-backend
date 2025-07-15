package org.example.newsapp.service;
import org.example.newsapp.controller.dto.CityDto;
import org.example.newsapp.infrastructure.entity.City;
import org.example.newsapp.infrastructure.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<City> get100MostPopulatedCities()
    {
        return cityRepository.findTop100ByOrderByPopulationDesc();
    }

    public List<String> findCitiesByQuery(String query) {
        if (query.contains(",")) {
            String[] parts = query.split(",");
            String cityPart = parts[0].trim();
            String statePart = parts.length > 1 ? parts[1].trim() : "";

            if (!statePart.isEmpty()) {
                return findCitiesByCityAndState(cityPart, statePart);
            }
            return findCitiesByCity(cityPart);
        }

        return cityRepository
                .findByCityStartingWithIgnoreCase(query)
                .stream()
                .map(city -> city.getCity() + ", " + city.getStateName())
                .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)))
                .stream()
                .limit(20)
                .toList();
    }


    public List<String> findCitiesByCity(String city) {
        return cityRepository
                .findByCityStartingWithIgnoreCase(city)
                .stream()
                .map(c -> c.getCity() + ", " + c.getStateName())
                .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)))
                .stream()
                .limit(20)
                .toList();
    }

    public List<String> findCitiesByCityAndState(String city, String state) {
        return cityRepository
                .findByCityStartingWithIgnoreCaseAndStateNameStartingWithIgnoreCase(city, state)
                .stream()
                .map(c -> c.getCity() + ", " + c.getStateName())
                .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)))
                .stream()
                .limit(20)
                .toList();
    }

    public CityDto getInfoByCityName(String cityName, String stateName) {
        return modelMapper.map(cityRepository.findCityByCityAndStateName(cityName, stateName), CityDto.class);
    }
}