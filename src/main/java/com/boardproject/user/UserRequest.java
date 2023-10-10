package com.boardproject.user;

import lombok.Getter;
import lombok.Setter;

public class UserRequest {

    @Getter @Setter
    public static class JoinDTO {
        private String username;
        private String password;
        private String email;
        private String nickName;

        public User toEntity(){
            return User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .nickName(nickName)
                    .roles("ROLE_USER")
                    .build();
        }
    }
}