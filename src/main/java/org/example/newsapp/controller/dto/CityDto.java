package org.example.newsapp.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

