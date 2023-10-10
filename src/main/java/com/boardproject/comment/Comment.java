package com.boardproject.comment;

import com.boardproject.board.Board;
import com.boardproject.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int depth; // 0:댓글, 1:대댓글

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent_comment_id")
    private List<Comment> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Builder
    public Comment(Long id, String content, int depth, List<Comment> children, Board board, User user, Timestamp createdAt) {
        this.id = id;
        this.content = content;
        this.depth = depth;
        this.children = children;
        this.board = board;
        this.user = user;
        this.createdAt = createdAt;
    }
}
