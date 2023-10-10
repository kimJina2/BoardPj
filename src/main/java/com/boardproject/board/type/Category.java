package com.boardproject.board.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public enum Category {
    GENERAL("새싹회원"), VIP("우수회원");
    private String name;

    public Category findName(String name) {
        return Category.valueOf(name);
    }
}
