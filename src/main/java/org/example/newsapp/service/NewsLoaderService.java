package org.example.newsapp.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.example.newsapp.controller.dto.ArticleDto;
import org.example.newsapp.infrastructure.entity.Article;
import org.example.newsapp.infrastructure.entity.City;
import org.example.newsapp.infrastructure.repository.ArticleRepository;
import org.example.newsapp.infrastructure.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class NewsLoaderService {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private NewsClassifierService classifierService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${news.api.key}")
    private String NEWS_API_KEY;

    @Value("${news.api.url}")
    private String NEWS_API_URL;


    public void fetchAndSaveArticles(String query, int page) throws Exception {
        String url = NEWS_API_URL +
                "?q=" + query.replace(" ", "%20") +
                "&language=en" +
                "&pageSize=3" +
                "&page=" + page +
                "&apiKey=" + NEWS_API_KEY;

        String jsonResponse = restTemplate.getForObject(url, String.class);
        NewsApiResponse response = objectMapper.readValue(jsonResponse, NewsApiResponse.class);
        List<ArticleDto> articles = response.getArticles();

        for (ArticleDto articleDto : articles) {

            if (!articleRepository.existsByTitle(articleDto.getTitle())) {

                NewsClassifierService.ClassificationResult result = classifierService.classify(articleDto);

                Article article = new Article();
                article.setSourceName(articleDto.getSource().getName());
                article.setAuthor(articleDto.getAuthor());
                article.setTitle(articleDto.getTitle());
                article.setDescription(articleDto.getDescription());
                article.setUrl(articleDto.getUrl());
                article.setUrlToImage(articleDto.getUrlToImage());
                article.setPublishedAt(articleDto.getPublishedAt());
                article.setContent("");
                article.setGlobal(result.isGlobal);

                if (!result.isGlobal && result.cityName != null && result.stateName != null) {
                    Optional<City> cityOptional = cityRepository.findCityByCityAndStateName(result.cityName, result.stateName);
                    cityOptional.ifPresent(article::setCity);
                    System.out.println("Classified as local: " + result.cityName + ", " + result.stateName);
                } else {
                    System.out.println("Classified as global news.");
                }

                System.out.println("Global? " + result.isGlobal + ", City: " + result.cityName + ", State: " + result.stateName);

                articleRepository.save(article);
                Thread.sleep(20001); // rate limiting
            }
        }
    }

    public void loadAndClassifyArticles() throws Exception {
        // 100 the most populated cities in the USA
        List<City> cities = cityRepository.findTop100ByOrderByPopulationDesc();

        // 20 topics are not related to cities
        List<String> globalTopics = List.of(
                "Science",
                "Medicine",
                "Space",
                "Energy",
                "AI",
                "Health",
                "Education",
                "Agriculture",
                "Programming",
                "Wildlife",
                "plastic%recycling%progress",
                "renewable%energy%adoption",
                "climate%change%solutions",
                "women%in%STEM",
                "youth%leadership%initiatives",
                "global%volunteer%work",
                "poverty%reduction%success",
                "access%to%clean%water",
                "medical%technology%innovations",
                "inclusive%education%programs",
                "cancer%research%breakthroughs",
                "global%health%improvement",
                "robotics%for%disability%aid",
                "ocean%cleanup%projects",
                "community%resilience%stories"
        );

        int page = 1;

        for (City city : cities) {
            fetchAndSaveArticles(city.getCity().replace(" ", "%20"), page);
            Thread.sleep(1000); // rate limits
        }

        /*for (String topic : globalTopics) {
            fetchAndSaveArticles(topic, page);
            Thread.sleep(1000);
        }*/
    }
}

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
class NewsApiResponse {
    private String status;
    private int totalResults;
    private List<ArticleDto> articles;
}
