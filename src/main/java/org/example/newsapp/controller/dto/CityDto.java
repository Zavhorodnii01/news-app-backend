package org.example.newsapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Data Transfer Object for city information
@Data
// No-argument constructor for CityDto
@NoArgsConstructor
// All-argument constructor for CityDto
@AllArgsConstructor
public class CityDto {
    private String id;
    private String city;
    private String stateName;
    private String countyName;
    private String lat;
    private String lng;
    private Long population;
    private String timezone;
}