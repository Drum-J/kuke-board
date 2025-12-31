package kuke.board.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("Comment Not Found");
    }

    public CommentNotFoundException(Long commentId) {
        super("Comment Not Found. commentId: " + commentId);
    }

    public CommentNotFoundException(Long articleId, Long commentId) {
        super("Comment Not Found. articleId: " + articleId + ", commentId: " + commentId);
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
