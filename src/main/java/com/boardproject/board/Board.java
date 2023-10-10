package com.boardproject.board;

import com.boardproject.comment.Comment;
import com.boardproject.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String thumbnail;

    @Column(nullable = false)
    @ColumnDefault("'GENERAL'")
    private String catagory; // 새싹:GENERAL, 우수:VIP

    @Column(name = "hide_flag", nullable = false)
    @ColumnDefault("0")
    private int isHide; // 0:보임, 1:숨김/삭제

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("createdAt asc")
    private List<Comment> commentList;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private Timestamp createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Builder
    public Board(Long id, String title, String content, User user, String thumbnail,
                 String catagory, int isHide, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.thumbnail = thumbnail;
        this.catagory = catagory;
        this.isHide = isHide;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
