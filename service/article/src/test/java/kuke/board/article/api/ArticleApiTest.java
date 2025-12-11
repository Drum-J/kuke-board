package kuke.board.article.api;

import kuke.board.article.dto.response.ArticlePageResponse;
import kuke.board.article.dto.response.ArticleResponse;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

//스프링부트 테스트가 아니라서 ArticleApplication을 실행 후 진행해야함
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
        ArticleResponse read = read(256561214487543808L);
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
        update(256561214487543808L);
        ArticleResponse response = read(256561214487543808L);
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
                .uri("/v1/articles/{articleId}", 256561214487543808L)
                .retrieve().toBodilessEntity();

        try {
            read(256561214487543808L);
        } catch (Exception e) {
            System.out.println("delete success, not read");
        }

    }

    @Test
    void readAllTest() throws Exception {
        //given
        ArticlePageResponse response = restClient.get()
                .uri("/v1/articles?boardId=1&page=50000&pageSize=30")
                .retrieve()
                .body(ArticlePageResponse.class);

        System.out.println("response.articleCount() = " + response.articleCount());
        for (ArticleResponse article : response.articles()) {
            System.out.println("articleId = " + article.articleId());
        }
    }

    record ArticleCreateRequest(String title, String content, Long writerId, Long boardId) {}
    record ArticleUpdateRequest(String title, String content) {}
}
