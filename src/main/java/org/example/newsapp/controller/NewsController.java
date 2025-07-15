package org.example.newsapp.controller;

import org.example.newsapp.controller.dto.ArticleDto;
import org.example.newsapp.service.NewsLoaderService;
import org.example.newsapp.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsLoaderService newsLoaderService;

    // Retrieves news articles specific to a city and state
    @GetMapping("/{cityName}/{stateName}")
    public ResponseEntity<List<ArticleDto>> getNewsArticlesByCity(
            @PathVariable String cityName,
            @PathVariable String stateName
    ) {
        List<ArticleDto> articleDtos = newsService.getNewsArticlesByCity(cityName, stateName);

        return ResponseEntity.ok(articleDtos);
    }

    // Retrieves global news articles
    @GetMapping("/global")
    public ResponseEntity<List<ArticleDto>> getGlobalNewsArticles() {
        List<ArticleDto> articleDtos = newsService.getGlobalNews();

        return ResponseEntity.ok(articleDtos);
    }

    // Loads and classifies more news articles
    @PostMapping("/loadAndClassifyArticles")
    public ResponseEntity<Boolean> loadAndClassifyMoreNews() throws Exception {

        boolean success = true;
        //try {
            newsLoaderService.loadAndClassifyArticles();
        //}
        //catch (Exception e) {
            success = false;
        //}

        return ResponseEntity.ok(success);
    }
}