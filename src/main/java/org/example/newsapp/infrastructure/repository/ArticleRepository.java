package org.example.newsapp.infrastructure.repository;

import org.example.newsapp.infrastructure.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

//Repository interface for managing Article entities.
//Extends JpaRepository to provide CRUD operations
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByCity_Id(String cityId);

    List<Article> findArticlesByGlobalTrue();

    Boolean existsByTitle(String title);
}