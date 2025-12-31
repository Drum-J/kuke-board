package kuke.board.comment.service;

import kuke.board.comment.dto.request.CommentCreateRequest;
import kuke.board.comment.dto.response.CommentResponse;
import kuke.board.comment.entity.Comment;
import kuke.board.comment.exception.CommentNotFoundException;
import kuke.board.comment.repository.CommentRepository;
import kuke.board.common.snowflake.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.function.Predicate.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final Snowflake snowflake = new Snowflake();
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequest request) {
        Comment parent = findParent(request.parentCommentId());
        Comment comment = commentRepository.save(
                Comment.create(
                        snowflake.nextId(),
                        request.content(),
                        parent == null ? null : parent.getCommentId(),
                        request.articleId(),
                        request.writerId()
                )
        );

        return CommentResponse.from(comment);
    }

    private Comment findParent(Long parentCommentId) {
        if (parentCommentId != null) {
            return commentRepository.findById(parentCommentId)
                    .filter(not(Comment::getDeleted))
                    .filter(Comment::isRoot)
                    .orElseThrow(() -> new CommentNotFoundException(parentCommentId));
        }

        return null;
    }

    public CommentResponse read(Long commentId) {
        return CommentResponse.from(commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId)));
    }

    @Transactional
    public void delete(Long commentId) {
        commentRepository.findById(commentId)
                .filter(not(Comment::getDeleted))
                .ifPresent(comment -> {
                    if (hasChildren(comment)) { // 하위 댓글이 있을 경우
                        comment.delete(); // 삭제 여부 변경
                    } else {
                        delete(comment);
                    }
                });
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countByArticleIdAndParentCommentId(
                comment.getArticleId(), comment.getCommentId(), Limit.of(2)) == 2;
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);
        // 상위 댓글을 찾아서 재귀적으로 삭제
        if (!comment.isRoot()) {
            commentRepository.findById(comment.getParentCommentId())
                    .filter(Comment::getDeleted) // 삭제 여부 체크
                    .filter(not(this::hasChildren)) // 하위 댓글 없으면
                    .ifPresent(this::delete); // 최종 삭제
        }
    }

}
