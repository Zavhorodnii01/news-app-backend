package org.example.newsapp.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.newsapp.controller.dto.ArticleDto;
import org.example.newsapp.infrastructure.entity.Article;
import org.example.newsapp.infrastructure.entity.City;
import org.example.newsapp.infrastructure.repository.ArticleRepository;
import org.example.newsapp.infrastructure.repository.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CityRepository cityRepository;

    // Retrieves news articles for a specific city by its name and state name
    public List<ArticleDto> getNewsArticlesByCity(String cityName, String stateName)
    {
        City city = cityRepository
                .findCityByCityAndStateName(cityName, stateName)
                .orElseThrow(() -> new EntityNotFoundException("City not found: " + cityName));

        String cityId = city.getId();
        List<Article> articles = articleRepository.findByCity_Id(cityId);

        return articles.stream().map(article -> modelMapper.map(article, ArticleDto.class)).toList();
    }

    // Retrieves global news articles that are marked as global
    public List<ArticleDto> getGlobalNews()
    {
        List<Article> articles = articleRepository.findArticlesByGlobalTrue();

        return articles.stream().map(article -> modelMapper.map(article, ArticleDto.class)).toList();
    }
}
