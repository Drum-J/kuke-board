package kuke.board.comment.dto.request;

public record CommentCreateRequest(Long articleId, String content, Long parentCommentId, Long writerId) {
}
