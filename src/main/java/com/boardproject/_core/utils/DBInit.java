package com.boardproject._core.utils;

import com.boardproject.board.Board;
import com.boardproject.board.BoardRepository;
import com.boardproject.comment.Comment;
import com.boardproject.comment.CommentRepository;
import com.boardproject.user.User;
import com.boardproject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DBInit {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Bean
    CommandLineRunner initDB(){
        return args -> {
            User ssar = User.builder()
                    .username("ssar")
                    .password(passwordEncoder.encode("1234"))
                    .email("ssar@nate.com")
                    .nickName("보리")
                    .roles("GENERAL")
                    .build();
            userRepository.save(ssar);

            User love = User.builder()
                    .username("love")
                    .password(passwordEncoder.encode("1234"))
                    .email("love@nate.com")
                    .nickName("러브")
                    .roles("GENERAL")
                    .build();
            userRepository.save(love);

            Board boardWithThumbnail = Board.builder()
                    .user(ssar)
                    .catagory("GENERAL")
                    .title("썸네일 있는 글")
                    .content("<p>썸네일이 있어요! " +
                            "<img src=\"/file/load?path=saved&name=7b5a4af3-26a9-417b-9d78-304d343012b3_0_0.jpg\" style=\"width: 975.733px;\">" +
                            "</p>")
                    .thumbnail("7b5a4af3-26a9-417b-9d78-304d343012b3_0_0.jpg")
                    .build();
            boardRepository.save(boardWithThumbnail);

            Board boardNoThumbnail = Board.builder()
                    .user(love)
                    .catagory("GENERAL")
                    .title("썸네일 없는 글")
                    .content("<p>썸네일이 없어요!</p>")
                    .build();
            boardRepository.save(boardNoThumbnail);

            Comment comment = Comment.builder()
                    .depth(0)
                    .board(boardWithThumbnail)
                    .user(love)
                    .content("와 멋져요")
                    .build();
            commentRepository.save(comment);
        };
    }
}