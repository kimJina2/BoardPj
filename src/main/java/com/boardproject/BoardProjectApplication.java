package com.boardproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.boardproject._core.security.CustomUserDetails;
import com.boardproject.user.User;
import com.boardproject.user.UserRepository;

@RestController
@SpringBootApplication
public class BoardProjectApplication {

    @GetMapping("/board")
    public String board(@AuthenticationPrincipal CustomUserDetails userDetails){
        System.out.println(userDetails.getUser().getNickName());
        return userDetails.getUsername();
    }

    public static void main(String[] args) {
        SpringApplication.run(BoardProjectApplication.class, args);
    }

}
