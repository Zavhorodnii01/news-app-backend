package org.example.newsapp.config;

import org.example.newsapp.controller.dto.ArticleDto;
import org.example.newsapp.infrastructure.entity.Article;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZonedDateTime;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

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
