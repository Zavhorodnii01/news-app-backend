package org.example.newsapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

// Data Transfer Object representing a news source
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SourceDto {

    private String id;
    private String name;
}
