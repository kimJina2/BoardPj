package com.boardproject.comment;

import com.boardproject._core.errors.exception.Exception404;
import com.boardproject.board.Board;
import com.boardproject.board.BoardRepository;
import com.boardproject.comment.dto.CommentRequest;
import com.boardproject.user.User;
import com.boardproject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void create(CommentRequest.CreateDTO createDTO) {
        Board boardPs = boardRepository.findById(createDTO.getBoardId())
                .orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        User userPS = userRepository.findById(createDTO.getUserId())
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));

        createDTO.setBoard(boardPs);
        createDTO.setUser(userPS);

        Comment comment = createDTO.toEntity();
        commentRepository.save(comment);

        // 대댓글이라면 부모 댓글에 답글로 추가
        if (createDTO.getDepth() == 1) {
            Comment commentPS = commentRepository.findById(createDTO.getParentId())
                    .orElseThrow(() -> new Exception404("댓글을 찾을 수 없습니다."));

            List<Comment> children = commentPS.getChildren();
            children.add(comment);
            commentRepository.save(commentPS);
        }
    }

    @Transactional
    public void delete(CommentRequest.DeleteDTO deleteDTO) {
        commentRepository.deleteById(deleteDTO.getId());
    }
}
