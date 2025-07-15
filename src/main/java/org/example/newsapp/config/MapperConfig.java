package org.example.newsapp.config;

import org.example.newsapp.controller.dto.ArticleDto;
import org.example.newsapp.infrastructure.entity.Article;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

// Configuration class for setting up the ModelMapper bean
@Configuration
public class MapperConfig {

    // Bean definition for ModelMapper, which is used to map between Article and ArticleDto objects
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Custom mappings for converting Article to ArticleDto
        mapper.addMappings(new PropertyMap<Article, ArticleDto>() {
            @Override
            protected void configure() {
                map().getSource().setName(source.getSourceName());

                using(ctx -> {
                    ZonedDateTime date = (ZonedDateTime) ctx.getSource();
                    return date == null ? null : date.toString();
                }).map(source.getPublishedAt(), destination.getPublishedAtString());
            }
        });

        return mapper;
    }
}
