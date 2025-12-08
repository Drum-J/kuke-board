package kuke.board.article.dto.request;

public record ArticleCreateRequest(String title, String content, Long writerId, Long boardId) {
}
