package kuke.board.article.controller;

import kuke.board.article.dto.request.ArticleInfiniteScrollRequest;
import kuke.board.article.dto.request.ArticleReadAllRequest;
import kuke.board.article.dto.response.ArticlePageResponse;
import kuke.board.article.service.ArticleService;
import kuke.board.article.dto.request.ArticleCreateRequest;
import kuke.board.article.dto.request.ArticleUpdateRequest;
import kuke.board.article.dto.response.ArticleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/v1/article/{articleId}")
    public ArticleResponse read(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    @GetMapping("/v1/articles")
    public ArticlePageResponse readAll(ArticleReadAllRequest request) {
        return articleService.readAll(request.boardId(), request.page(), request.pageSize());
    }

    @GetMapping("/v1/articles/infinite-scroll")
    public List<ArticleResponse> readAllInfiniteScroll(ArticleInfiniteScrollRequest request) {
        return articleService.readAllInfiniteScroll(request.boardId(), request.pageSize(), request.lastArticleId());
    }

    @PostMapping("/v1/articles")
    public ArticleResponse create(@RequestBody ArticleCreateRequest request) {
        return articleService.create(request);
    }

    @PutMapping("/v1/articles/{articleId}")
    public ArticleResponse update(@PathVariable Long articleId, @RequestBody ArticleUpdateRequest request) {
        return articleService.update(articleId, request);
    }

    @DeleteMapping("/v1/articles/{articleId}")
    public void delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }
}
