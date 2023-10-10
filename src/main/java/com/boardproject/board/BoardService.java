package com.boardproject.board;

import com.boardproject.File.FileService;
import com.boardproject._core.errors.exception.Exception404;
import com.boardproject._core.utils.JsoupParser;
import com.boardproject.board.dto.BoardRequest;
import com.boardproject.board.dto.BoardResponse;
import com.boardproject.user.User;
import com.boardproject.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional
    public BoardResponse.CreateDTO create(BoardRequest.CreateFormDTO createFormDTO) {
        // 사진 저장
        fileService.moveFileToSave();

        // summernote content 내 파일 경로 temp->saved 로 치환
        String replaced = createFormDTO.getContent().replaceAll("path=temp", "path=saved");
        createFormDTO.setContent(replaced);

        // 작성자 설정
        User userPS = userRepository.findById(createFormDTO.getUserId())
                .orElseThrow(() -> new Exception404("유저를 찾을 수 없습니다."));
        createFormDTO.setUser(userPS);

        Board board = createFormDTO.toEntity();
        Board boardPS = boardRepository.save(board);
        return new BoardResponse.CreateDTO(boardPS);
    }

    @Transactional
    public BoardResponse.DetailsDTO getDetailsById(Long boardId) {
        Optional<Board> boardOP = boardRepository.findById(boardId);
        Board boardPS = boardOP.orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        return new BoardResponse.DetailsDTO(boardPS);
    }

    @Transactional
    public BoardResponse.UpdateDTO update(BoardRequest.UpdateDTO updateDTO) {
        Optional<Board> boardOP = boardRepository.findById(updateDTO.getId());
        Board boardPS = boardOP.orElseThrow(() -> new Exception404("게시글을 찾을 수 없습니다."));

        // 사진 저장
        fileService.moveFileToSave();

        // summernote content 내 파일 경로 temp->saved 로 치환
        String replaced = updateDTO.getContent().replaceAll("path=temp", "path=saved");
        updateDTO.setContent(replaced);

        updateDTO.setEntity(boardPS);

        return new BoardResponse.UpdateDTO(boardPS);
    }

    @Transactional
    public void delete(BoardRequest.DeleteDTO deleteDTO) {
        boardRepository.deleteById(deleteDTO.getId());

        // 게시글 내 이미지 모두 삭제
        List<String> parsedImgNames = JsoupParser.parseImgName(deleteDTO.getContent());
        parsedImgNames.forEach(fileService::delete);
    }

    @Transactional
    public List<Board> findByCategory(String category) {
        List<Board> boards = boardRepository.findAll();

        return null;
    }

    @Transactional
    public Page<Board> findAll(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards;
    }

    @Transactional
    public BoardResponse.ListDTO getDetailsByCategory(String category) {
        return null;
    }
}
