package org.example.newsapp.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.time.ZonedDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ArticleDto {

    private Source source;

    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;

    @JsonProperty("publishedAt")
    private String publishedAtString;

    private String content;

    @JsonIgnore
    public ZonedDateTime getPublishedAt() {
        return ZonedDateTime.parse(publishedAtString);
    }

    @Data
    public static class Source {
        private String id;
        private String name;
    }
}


