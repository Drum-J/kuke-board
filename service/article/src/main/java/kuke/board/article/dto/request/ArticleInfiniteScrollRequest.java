package kuke.board.article.dto.request;

public record ArticleInfiniteScrollRequest(Long boardId, Long pageSize, Long lastArticleId) {
}
