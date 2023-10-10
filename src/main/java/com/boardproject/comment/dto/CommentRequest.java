package com.boardproject.comment.dto;

import com.boardproject.board.Board;
import com.boardproject.comment.Comment;
import com.boardproject.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class CommentRequest {
    @Getter
    @Setter
    public static class CreateDTO {
        @NotNull
        private Long boardId;

        @NotNull
        private Long userId;

        private int depth;

        private Long parentId;

        @NotEmpty
        @Length(max = 50)
        private String content;

        private Board board;

        private User user;

        private List<Comment> children;

        public Comment toEntity() {
            return Comment.builder()
                    .board(board)
                    .user(user)
                    .depth(depth)
                    .children(children)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class DeleteDTO {
        @NotNull
        private Long id;
        @NotNull
        private Long boardId;
    }
}
