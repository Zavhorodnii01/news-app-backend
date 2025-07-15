package org.example.newsapp.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// Represents a city entity in the news application
@Getter
@Setter
@Entity
@Table(name = "cities")
public class City {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "city", length = 255)
    private String city;

    @Column(name = "city_ascii", length = 255)
    private String cityAscii;

    @Column(name = "state_id", length = 50)
    private String stateId;

    @Column(name = "state_name", length = 255)
    private String stateName;

    @Column(name = "county_fips", length = 50)
    private String countyFips;

    @Column(name = "county_name", length = 255)
    private String countyName;

    @Column(name = "lat", length = 50)
    private String lat;

    @Column(name = "lng", length = 50)
    private String lng;

    @Column(name = "population")
    private Long population;

    @Column(name = "population_proper", length = 50)
    private String populationProper;

    @Column(name = "density", length = 50)
    private String density;

    @Column(name = "source", length = 255)
    private String source;

    @Column(name = "incorporated", length = 50)
    private String incorporated;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "zips", columnDefinition = "TEXT")
    private String zips;

}