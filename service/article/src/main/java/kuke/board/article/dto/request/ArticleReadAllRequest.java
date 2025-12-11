package kuke.board.article.dto.request;

public record ArticleReadAllRequest(Long boardId, Long page, Long pageSize) {
}
