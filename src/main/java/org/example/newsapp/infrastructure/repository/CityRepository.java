package org.example.newsapp.infrastructure.repository;

import org.example.newsapp.infrastructure.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import java.util.Optional;

// Repository interface for managing City entities.
//Extends JpaRepository to provide CRUD operations
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findCityByCityAndStateName(String cityName, String stateName);

    List<City> findTop100ByOrderByPopulationDesc();

    List<City> findByCityStartingWithIgnoreCase(String prefix);

    List<City> findByCityStartingWithIgnoreCaseAndStateNameStartingWithIgnoreCase(String city, String state);
}