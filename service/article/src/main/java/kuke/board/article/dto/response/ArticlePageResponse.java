package kuke.board.article.dto.response;

import java.util.List;

public record ArticlePageResponse(List<ArticleResponse> articles, Long articleCount) {
}
