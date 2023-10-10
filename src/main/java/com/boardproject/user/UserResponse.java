package com.boardproject.user;

import lombok.Getter;
import lombok.Setter;

public class UserResponse {
    @Getter
    public static class BoardWriterDTO {
        private Long id;
        private String nickName;

        public BoardWriterDTO(User user) {
            this.id = user.getId();
            this.nickName = user.getNickName();
        }
    }
}