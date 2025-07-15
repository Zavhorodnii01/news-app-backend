package org.example.newsapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

// Data Transfer Object representing an article
@Data
public class ArticleDto {

    // Source of the article
    private Source source;

    // Author of the article
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;

    // Published date of the article in string format
    @JsonProperty("publishedAt")
    private String publishedAtString;

    // Content of the article
    private String content;

    // Method to convert the publishedAtString to a ZonedDateTime object
    @JsonIgnore
    public ZonedDateTime getPublishedAt() {
        return ZonedDateTime.parse(publishedAtString);
    }

    // Nested Data Transfer Object representing the source of an article
    @Data
    public static class Source {
        private String id;
        private String name;
    }
}
