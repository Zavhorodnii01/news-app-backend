package org.example.newsapp.infrastructure.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

// Represents an article entity in the news application
@Data
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sourceName;
    private String author;
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url;

    @Column(name = "url_to_image", length = 1000)
    private String urlToImage;

    private ZonedDateTime publishedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private boolean global;
}
