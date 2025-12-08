package kuke.board.article.api;

import kuke.board.article.service.request.ArticleCreateRequest;
import kuke.board.article.service.response.ArticleResponse;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

public class ArticleApiTest {
    RestClient restClient = RestClient.create("http://localhost:9000/");

    @Test
    void createTest() throws Exception {
        ArticleResponse response = create(
                new ArticleCreateRequest("hi", "my content", 1L, 1L)
        );
        System.out.println("create response = " + response);
    }

    ArticleResponse create(ArticleCreateRequest request) {
        return restClient.post()
                .uri("/v1/articles")
                .body(request)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void readTest() throws Exception {
        ArticleResponse read = read(256321777401872384L);
        System.out.println("read response = " + read);
    }

    ArticleResponse read(Long articleId) {
        return restClient.get()
                .uri("/v1/article/{articleId}", articleId)
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void updateTest() throws Exception {
        update(256321777401872384L);
        ArticleResponse response = read(256321777401872384L);
        System.out.println("update response = " + response);
    }

    void update(Long articleId) {
        restClient.put()
                .uri("/v1/articles/{articleId}", articleId)
                .body(new ArticleUpdateRequest("hi 2", "my content 2"))
                .retrieve()
                .body(ArticleResponse.class);
    }

    @Test
    void deleteTest() throws Exception {
        restClient.delete()
                .uri("/v1/articles/{articleId}", 256321777401872384L)
                .retrieve().toBodilessEntity();
        System.out.println("delete response = " + read(256321777401872384L)); // 삭제 되었기 때문에 에러 발생

    }

    record ArticleCreateRequest(String title, String content, Long writerId, Long boardId) {}
    record ArticleUpdateRequest(String title, String content) {}
}
