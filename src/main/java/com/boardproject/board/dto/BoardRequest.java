package com.boardproject.board.dto;

import com.boardproject.board.Board;
import com.boardproject.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BoardRequest {
    @Getter
    @Setter
    public static class CreateFormDTO {
        @NotNull
        private Long userId;

        @NotEmpty
        private String catagory;

        private String thumbnail;

        @NotEmpty
        @Length(max = 100)
        private String title;

        @NotEmpty
        @Length(max = 1000)
        private String content;

        private User user;

        public Board toEntity() {
            return Board.builder()
                    .user(user)
                    .catagory(catagory)
                    .thumbnail(thumbnail)
                    .title(title)
                    .content(content)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class UpdateFormDTO {
        @NotNull
        private Long id;

        @NotEmpty
        private String title;

        @NotEmpty
        private String content;

        private String thumbnail;
    }

    @Getter
    @Setter
    public static class UpdateDTO {
        @NotNull
        private Long id;

        private String thumbnail;

        @NotEmpty
        @Length(max = 100)
        private String title;

        @NotEmpty
        @Length(max = 1000)
        private String content;

        public void setEntity(Board boardPS) {
            boardPS.setThumbnail(thumbnail);
            boardPS.setTitle(title);
            boardPS.setContent(content);
        }
    }

    @Getter
    @Setter
    public static class DeleteDTO {
        @NotNull
        private Long id;

        private String content;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ListDTO {
        private Long id;
        private String title;
        private String content;
        private String thumbnail;
        private String writerNickName;

        public static ListDTO toEntity(Board board, User user) {
            return ListDTO.builder()
                    .id(board.getId())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .thumbnail(board.getThumbnail())
                    .writerNickName(user.getNickName())
                    .build();
        }
    }
}
