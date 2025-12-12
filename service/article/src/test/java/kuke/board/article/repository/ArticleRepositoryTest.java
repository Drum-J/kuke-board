package kuke.board.article.repository;

import kuke.board.article.entity.Article;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ArticleRepositoryTest {

    @Autowired ArticleRepository articleRepository;

    @Test
    void findAllTest() throws Exception {
        List<Article> articles = articleRepository.findAll(1L, 1_499_970L, 30L);
        log.info("articles.size = {}", articles.size());

        for (Article article : articles) {
            log.info("article = {}", article);
        }
    }

    @Test
    void countTest() throws Exception {
        Long count = articleRepository.count(1L, 10_000L);
        log.info("count = {}", count);
    }

    @Test
    void findInfiniteScrollTest() throws Exception {
        List<Article> articles = articleRepository.findAllInfiniteScroll(1L, 30L);
        for (Article article : articles) {
            log.info("articles first infiniteScroll = {}", article.getArticleId());
        }

        Long lastArticleId = articles.getLast().getArticleId();
        articles = articleRepository.findAllInfiniteScroll(1L, 30L, lastArticleId);
        for (Article article : articles) {
            log.info("article second infiniteScroll = {}", article.getArticleId());
        }

    }
}