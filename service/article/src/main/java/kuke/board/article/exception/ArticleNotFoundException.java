package kuke.board.article.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super("Article Not Found");
    }

    public ArticleNotFoundException(Long articleId) {
        super("Article Not Found. articleId: " + articleId);
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }
}
