package kuke.board.comment.service;

import kuke.board.comment.entity.Comment;
import kuke.board.comment.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Limit;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks CommentService commentService;
    @Mock CommentRepository commentRepository;

    @Test
    @DisplayName("삭제할 댓글이 자식이 있으면, 삭제 표시만 한다.")
    void deleteShouldMarkDeletedIfHasChildren() throws Exception {
        //given
        Long articleId = 1L;
        Long commentId = 2L;
        Comment comment = createComment(articleId, commentId);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentRepository.countByArticleIdAndParentCommentId(articleId, commentId, Limit.of(2))).willReturn(2L);

        //when
        commentService.delete(commentId);

        //then
        verify(comment).delete();
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제되지 않은 부모면, 하위 댓글만 삭제한다.")
    void deleteShouldDeletedChildOnlyIfNotDeletedParent() throws Exception {
        //given
        Long articleId = 1L;
        Long commentId = 2L;
        Long parentCommentId = 1L;

        Comment comment = createComment(articleId, commentId, parentCommentId);
        given(comment.isRoot()).willReturn(false);

        Comment parent = mock(Comment.class);
        given(parent.getDeleted()).willReturn(false); // 상위(부모) 댓글은 삭제되지 않음.

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentRepository.countByArticleIdAndParentCommentId(articleId, commentId, Limit.of(2))).willReturn(1L);
        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parent));

        //when
        commentService.delete(commentId);

        //then
        verify(commentRepository).delete(comment);
        verify(commentRepository, never()).delete(parent);
    }

    @Test
    @DisplayName("하위 댓글이 삭제되고, 삭제된 부모면, 재귀적으로 모두 삭제한다.")
    void deleteShouldDeleteAllRecursivelyIfDeletedParent() throws Exception {
        //given
        Long articleId = 1L;
        Long commentId = 2L;
        Long parentCommentId = 1L;

        Comment comment = createComment(articleId, commentId, parentCommentId);
        given(comment.isRoot()).willReturn(false);

        Comment parent = createComment(articleId, parentCommentId);
        given(parent.isRoot()).willReturn(true);
        given(parent.getDeleted()).willReturn(true); // 상위(부모) 댓글 삭제됨.

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));
        given(commentRepository.countByArticleIdAndParentCommentId(articleId, commentId, Limit.of(2))).willReturn(1L);

        given(commentRepository.findById(parentCommentId)).willReturn(Optional.of(parent));
        given(commentRepository.countByArticleIdAndParentCommentId(articleId, parentCommentId, Limit.of(2))).willReturn(1L);

        //when
        commentService.delete(commentId);

        //then
        verify(commentRepository).delete(comment);
        verify(commentRepository).delete(parent);
    }

    private Comment createComment(Long articleId, Long commentId) {
        Comment comment = mock(Comment.class);
        given(comment.getArticleId()).willReturn(articleId);
        given(comment.getCommentId()).willReturn(commentId);

        return comment;
    }

    private Comment createComment(Long articleId, Long commentId, Long parentCommentId) {
        Comment comment = createComment(articleId, commentId);
        given(comment.getParentCommentId()).willReturn(parentCommentId);

        return comment;
    }
}